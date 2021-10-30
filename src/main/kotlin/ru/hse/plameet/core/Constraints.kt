package ru.hse.plameet.core

interface Constraint {
    fun calcPenalty(schedule: Schedule): Double
}

interface BooleanConstraint : Constraint {
    val weight: Double

    override fun calcPenalty(schedule: Schedule) = if (isSatisfied(schedule)) weight else .0

    fun isSatisfied(schedule: Schedule): Boolean
}

interface RequiredConstraint : BooleanConstraint {
    override val weight: Double
        get() = Double.MAX_VALUE
}
