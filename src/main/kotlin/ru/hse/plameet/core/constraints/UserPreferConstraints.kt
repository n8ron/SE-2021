package ru.hse.plameet.core.constraints

import ru.hse.plameet.core.Schedule
import ru.hse.plameet.core.TimeRange
import ru.hse.plameet.core.User

class UserPreferConstraints(av: Map<User, List<Pair<TimeRange, Double>>>) : Constraint {
    private val usersPriorities: MutableMap<User, List<Pair<TimeRange, Double>>> = HashMap()

    init {
        av.forEach { (user, priorities) -> usersPriorities[user] = priorities.sortedBy { it.first.begin.units } }
    }

    override fun calcPenalty(schedule: Schedule): Double {
        var result = 0.0
        for (timedEvent in schedule.events) {
            for (participant in timedEvent.event.participants) {
                if (!usersPriorities.containsKey(participant)) {
                    continue
                }
                val eventEnd = timedEvent.time.units + timedEvent.event.duration.units
                val priorities = usersPriorities[participant]
                var lowerBound = priorities!!.binarySearch { it.first.begin.units - timedEvent.time.units }
                if (lowerBound < 0) {
                    lowerBound = -lowerBound - 2
                }
                if (lowerBound != -1) {
                    result += priorities[lowerBound].second
                }
                lowerBound++
                for (i in lowerBound until priorities.size) {
                    if (priorities[i].first.begin.units > eventEnd) {
                        break
                    }
                    if (priorities[i].first.end.units > eventEnd) {
                        result += priorities[i].second
                        break
                    }
                    result += priorities[i].second
                    lowerBound++
                }
            }
        }
        return result
    }
}
