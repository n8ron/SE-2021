package ru.hse.plameet.core.constraints

import ru.hse.plameet.core.*

/**
 * Constraint marking time during which user prefer to be free. Penalty is the sum of all dissatisfied ranges.
 */
class UserPreferConstraints(av: Map<User, List<Pair<TimeRange, Double>>>) : Constraint {
    val usersPriorities: MutableMap<User, List<Pair<TimeRange, Double>>> = HashMap()

    init {
        av.forEach { (user, priorities) -> usersPriorities[user] = priorities.sortedBy { it.first.begin.units } }
    }

    override fun calcPenalty(schedule: Schedule): Double {
        return schedule.events.sumOf { calcPenaltyEvent(it) }
    }

    fun calcPenaltyEvent(event: TimedEvent): Double {
        var result = .0
        for (participant in event.event.participants) {
            usersPriorities[participant]?.let {
                result += calcPenaltyEventUser(event.timeRange(), it)
            }
        }
        return result
    }

    companion object {
        fun calcPenaltyEventUser(event: TimeRange, priorities: List<Pair<TimeRange, Double>>): Double {
            var lowerBound = priorities.binarySearch { it.first.begin.units - event.begin.units }
            if (lowerBound < 0) {
                lowerBound = -lowerBound - 2
            } else {
                while (lowerBound >= 0 && priorities[lowerBound].first.begin == event.begin) {
                    lowerBound--
                }
            }

            var result = .0

            if (lowerBound != -1 && priorities[lowerBound].first.end.units >= event.begin.units) {
                result += priorities[lowerBound].second
            }
            lowerBound++

            while (lowerBound < priorities.size && priorities[lowerBound].first.begin.units <= event.end.units) {
                result += priorities[lowerBound].second
                lowerBound++
            }

            return result
        }
    }
}
