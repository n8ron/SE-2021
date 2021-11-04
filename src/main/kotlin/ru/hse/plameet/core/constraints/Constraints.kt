package ru.hse.plameet.core.constraints

import ru.hse.plameet.core.Schedule

interface Constraint {
    fun calcPenalty(schedule: Schedule): Double
}

interface BooleanConstraint : Constraint {
    val weight: Double

    override fun calcPenalty(schedule: Schedule) = if (isSatisfied(schedule)) .0 else weight

    fun isSatisfied(schedule: Schedule): Boolean
}

interface RequiredConstraint : BooleanConstraint {
    override val weight: Double
        get() = Double.MAX_VALUE
}
