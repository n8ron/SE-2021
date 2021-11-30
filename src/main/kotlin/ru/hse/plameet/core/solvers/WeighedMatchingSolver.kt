package ru.hse.plameet.core.solvers

import org.jgrapht.alg.interfaces.MatchingAlgorithm
import org.jgrapht.alg.matching.MaximumWeightBipartiteMatching
import org.jgrapht.graph.DefaultUndirectedWeightedGraph
import ru.hse.plameet.core.*
import ru.hse.plameet.core.constraints.Constraint
import ru.hse.plameet.core.constraints.TimeSlotsConstraint
import ru.hse.plameet.core.constraints.UserAvailabilityConstraint
import ru.hse.plameet.core.constraints.UserPreferConstraints

/**
 * Solver using min-cost pairing algorithm.
 *
 * Requires one TimeSlotsConstraint.
 * Supports one UserAvailabilityConstraint.
 * Supports one UserPreferConstraint.
 */
class WeighedMatchingSolver private constructor(
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
        val graph = DefaultUndirectedWeightedGraph<Int, IntEdge>(null, null)
        for (i in 0 until events.size + slots.size) {
            graph.addVertex(i)
        }

        events.forEachIndexed { i, event ->
            slots.forEachIndexed { j, slot ->
                if (canMatch(event, slot)) {
                    val edge = IntEdge(i, j)
                    graph.addEdge(i, events.size + j, edge)
                    graph.setEdgeWeight(
                        edge,
                        -(constraints.prefer?.calcPenaltyEvent(TimedEvent(event, slot.begin)) ?: 0.0)
                    )
                }
            }
        }

        if (graph.edgeSet().size == 0) {
            return MatchingAlgorithm.MatchingImpl(graph, emptySet(), .0)
        }

        val maxPenalty = -graph.edgeSet().minOf { graph.getEdgeWeight(it) }
        val eventWeight = 1.0 + maxPenalty * minOf(slots.size, events.size)

        graph.edgeSet().forEach {
            graph.setEdgeWeight(it, eventWeight + graph.getEdgeWeight(it))
        }

        val leftPartition = RangeSet(0, events.size)
        val rightPartition = RangeSet(events.size, events.size + slots.size)

        return MaximumWeightBipartiteMatching(graph, leftPartition, rightPartition).matching
    }

    private fun canMatch(event: Event, slot: TimeRange): Boolean {
        if (event.duration.units > slot.duration.units) {
            return false
        }
        constraints.availability?.let { availability ->
            if (!event.participants.all { availability.av[it]?.let(slot::subrangeSorted) == true }) {
                return false
            }
        }
        return true
    }

    private data class IntEdge(val left: Int, val right: Int)

    private data class KnownConstraints(
        val slots: TimeSlotsConstraint,
        val availability: UserAvailabilityConstraint?,
        val prefer: UserPreferConstraints?
    )

    companion object : Solver {
        override fun solve(events: List<Event>, constraints: List<Constraint>): Schedule? {
            val slots = mutableListOf<TimeSlotsConstraint>()
            val availabilities = mutableListOf<UserAvailabilityConstraint>()
            val prefers = mutableListOf<UserPreferConstraints>()
            for (constraint in constraints) {
                when (constraint) {
                    is TimeSlotsConstraint -> slots.add(constraint)
                    is UserAvailabilityConstraint -> availabilities.add(constraint)
                    is UserPreferConstraints -> prefers.add(constraint)
                }
            }

            val slot = when {
                slots.isEmpty() -> {
                    throw IllegalArgumentException("Must contain at least one slotsConstraint")
                }
                slots.size > 1 -> {
                    throw IllegalArgumentException("Multiple TimeSlotsConstraint can't be merged automatically")
                }
                else -> slots.first()
            }

            val availability = when {
                availabilities.size > 1 -> {
                    throw IllegalArgumentException("Merging UserAvailabilityConstraint is not yet implemented")
                }
                availabilities.size == 1 -> availabilities.first()
                else -> null
            }

            val prefer = when {
                prefers.size > 1 -> {
                    throw IllegalArgumentException("Merging UserPreferConstraints is not yet implemented")
                }
                prefers.size == 1 -> prefers.first()
                else -> null
            }

            val knownConstraints = KnownConstraints(slot, availability, prefer)
            return WeighedMatchingSolver(events, knownConstraints, constraints).solve()
        }
    }
}
