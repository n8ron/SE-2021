package ru.hse.plameet.core.constraints

import ru.hse.plameet.core.Event
import ru.hse.plameet.core.Schedule

/**
 * Constraint for marking mandatory events
 */
class EventsRequiredConstraint(private val events: Collection<Event>) : RequiredConstraint {

    override fun isSatisfied(schedule: Schedule): Boolean {
        val scheduleEvents = HashSet(schedule.events.map { it.event })
        return scheduleEvents.containsAll(events)
    }
}
