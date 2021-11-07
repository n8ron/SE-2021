package ru.hse.plameet.core.solvers

import org.jgrapht.alg.interfaces.MatchingAlgorithm
import org.jgrapht.alg.matching.HopcroftKarpMaximumCardinalityBipartiteMatching
import org.jgrapht.graph.DefaultUndirectedGraph
import ru.hse.plameet.core.*
import ru.hse.plameet.core.constraints.Constraint
import ru.hse.plameet.core.constraints.TimeSlotsConstraint

/**
 * Solver using classic pairing algorithm.
 *
 * Requires one TimeSlotsConstraint.
 */
class MatchingSolver private constructor(
    private val events: List<Event>,
    private val constraints: KnownConstraints,
    private val allConstraints: List<Constraint>
) {

    private fun solve(): Schedule? {
        val slots = constraints.slots.sortedSlots

        val matching = buildMatching(slots)

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

        val leftPartition = RangeSet(0, events.size)
        val rightPartition = RangeSet(events.size, events.size + slots.size)

        return HopcroftKarpMaximumCardinalityBipartiteMatching(graph, leftPartition, rightPartition).matching
    }

    private data class IntEdge(val left: Int, val right: Int)

    private data class KnownConstraints(
        val slots: TimeSlotsConstraint
    )

    companion object : Solver {
        override fun solve(events: List<Event>, constraints: List<Constraint>): Schedule? {
            val slots = mutableListOf<TimeSlotsConstraint>()
            for (constraint in constraints) {
                when (constraint) {
                    is TimeSlotsConstraint -> slots.add(constraint)
                }
            }

            val slot = if (slots.isEmpty()) {
                throw IllegalArgumentException("Must contain at least one slotsConstraint")
            } else if (slots.size > 1) {
                throw IllegalArgumentException("Multiple TimeSlotsConstraint can't be merged automatically")
            } else {
                slots.first()
            }

            val knownConstraints = KnownConstraints(slot)
            return MatchingSolver(events, knownConstraints, constraints).solve()
        }
    }
}
