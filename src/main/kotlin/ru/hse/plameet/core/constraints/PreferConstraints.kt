package ru.hse.plameet.core.constraints

import ru.hse.plameet.core.Schedule
import ru.hse.plameet.core.TimeRange
import ru.hse.plameet.core.User

class PreferConstraints(val av: Map<User, List<Pair<TimeRange, Double>>>) : Constraint {
    override fun calcPenalty(schedule: Schedule): Double {
        TODO("Not yet implemented")
    }
}
