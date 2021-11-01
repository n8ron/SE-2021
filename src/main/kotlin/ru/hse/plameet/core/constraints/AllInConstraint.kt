package ru.hse.plameet.core.constraints

import ru.hse.plameet.core.Event
import ru.hse.plameet.core.Schedule

class AllInConstraint(val events: Collection<Event>) : RequiredConstraint {
    override fun isSatisfied(schedule: Schedule): Boolean {
        TODO("Not yet implemented")
    }
}
