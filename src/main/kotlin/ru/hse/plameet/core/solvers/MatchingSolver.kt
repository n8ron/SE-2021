package ru.hse.plameet.core.solvers

import org.jgrapht.alg.interfaces.MatchingAlgorithm
import org.jgrapht.alg.matching.HopcroftKarpMaximumCardinalityBipartiteMatching
import org.jgrapht.graph.DefaultUndirectedGraph
import ru.hse.plameet.core.Event
import ru.hse.plameet.core.Schedule
import ru.hse.plameet.core.TimeRange
import ru.hse.plameet.core.TimedEvent
import ru.hse.plameet.core.constraints.Constraint
import ru.hse.plameet.core.constraints.SlotsConstraint

class MatchingSolver private constructor(
    private val events: List<Event>,
    private val constraints: KnownConstraints,
    private val allConstraints: List<Constraint>
) {

    private fun solve(): Schedule? {
        val slots = constraints.slots.first().sortedSlots

        val matching = buildMatching(slots)

        // must be removed after implementing constraint which check that all event in schedule
        if (matching.edges.size < events.size) return null

        val timedEvents = mutableListOf<TimedEvent>()
        matching.forEach { match ->
            timedEvents.add(TimedEvent(events[match.left], slots[match.right].begin))
        }
        val schedule = Schedule(timedEvents)

        val penalty = allConstraints.sumOf { it.calcPenalty(schedule) }

        return if (penalty < Double.MAX_VALUE) schedule else null
    }

    private fun buildMatching(slots: List<TimeRange>): MatchingAlgorithm.Matching<Int, IntEdge> {
        val graph = DefaultUndirectedGraph<Int, IntEdge>(null, null, false)
        for (i in 0 until events.size + slots.size) {
            graph.addVertex(i)
        }

        events.forEachIndexed { i, event ->
            slots.forEachIndexed { j, slot ->
                if (event.duration.units <= slot.duration.units) {
                    graph.addEdge(i, events.size + j, IntEdge(i, j))
                }
            }
        }

        val leftPartition = HashSet<Int>()
        for (i in events.indices) {
            leftPartition.add(i)
        }

        val rightPartition = HashSet<Int>()
        for (i in slots.indices) {
            rightPartition.add(events.size + i)
        }

        return HopcroftKarpMaximumCardinalityBipartiteMatching(graph, leftPartition, rightPartition).matching
    }

    private data class IntEdge(val left: Int, val right: Int)

    private data class KnownConstraints(
        val slots: MutableList<SlotsConstraint> = mutableListOf()
    )

    companion object : Solver {
        override fun solve(events: List<Event>, constraints: List<Constraint>): Schedule? {
            val knownConstraints = KnownConstraints()
            for (constraint in constraints) {
                when (constraint) {
                    is SlotsConstraint -> knownConstraints.slots.add(constraint)
                }
            }

            if (knownConstraints.slots.size != 1) {
                throw IllegalArgumentException("May solve only with one slots constraint")
            }

            return MatchingSolver(events, knownConstraints, constraints).solve()
        }
    }
}
