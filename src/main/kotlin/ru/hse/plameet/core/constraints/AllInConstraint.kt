package ru.hse.plameet.core.constraints

import ru.hse.plameet.core.Event
import ru.hse.plameet.core.Schedule

class AllInConstraint(private val events: Collection<Event>) : RequiredConstraint {

    override fun isSatisfied(schedule: Schedule): Boolean {
        val scheduleEvents = HashSet(schedule.events.map { it.event })
        return scheduleEvents.containsAll(events)
    }
}
