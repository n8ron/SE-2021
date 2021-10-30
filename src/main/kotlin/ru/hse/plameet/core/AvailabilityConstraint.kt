package ru.hse.plameet.core

class AvailabilityConstraint(av: Map<User, List<TimeRange>>) : RequiredConstraint {

    private val av = av.mapValues {
        it.value.sortedWith(
            compareBy { r -> r.begin.units }
        )
    }

    override fun isSatisfied(schedule: Schedule): Boolean {
        val user2schedule: MutableMap<User, MutableList<TimeRange>> = mutableMapOf()
        for (event in schedule.events) {
            val eventTimeRange = event.time.during(event.event.duration)
            for (participant in event.event.participants) {
                user2schedule.getOrPut(participant, { mutableListOf() }).add(eventTimeRange)
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
