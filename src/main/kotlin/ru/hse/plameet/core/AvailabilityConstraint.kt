package ru.hse.plameet.core

class AvailabilityConstraint(val av: Map<User, List<TimeRange>>) : RequiredConstraint {
    override fun isSatisfied(schedule: Schedule): Boolean {
        TODO("Not yet implemented")
    }
}
