package ru.hse.plameet.core.constraints

import ru.hse.plameet.core.*

/**
 * Constraint marks that user can participate in events only in provided time ranges
 */
class UserAvailabilityConstraint(av: Map<User, List<TimeRange>>) : RequiredConstraint {

    val av = av.mapValues {
        it.value.sortedWith(
            compareBy { r -> r.begin.units }
        )
    }

    init {
        av.values.forEach {
            if (intersectsSorted(it)) {
                throw IllegalArgumentException("Users' available slots should not overlap")
            }
        }
    }

    override fun isSatisfied(schedule: Schedule): Boolean {
        val user2schedule: MutableMap<User, MutableList<TimeRange>> = mutableMapOf()
        for (event in schedule.events) {
            val eventTimeRange = event.time.during(event.event.duration)
            for (participant in event.event.participants) {
                user2schedule.getOrPut(participant) { mutableListOf() }.add(eventTimeRange)
            }
        }
        for ((user, eventsRanges) in user2schedule) {
            eventsRanges.sortWith(
                compareBy { it.begin.units }
            )
            if (intersectsSorted(eventsRanges) || !av.containsKey(user)) {
                return false
            }
            var iSlots = 0
            for (eventRange in eventsRanges) {
                var isSatisfied = false
                val userSlots = av[user]!!
                while (iSlots < userSlots.size) {
                    if (userSlots[iSlots++].contains(eventRange)) {
                        isSatisfied = true
                        break
                    }
                }
                if (!isSatisfied) {
                    return false
                }
            }
        }
        return true
    }
}
