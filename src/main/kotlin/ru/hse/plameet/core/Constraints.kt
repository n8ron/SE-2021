package ru.hse.plameet.core

import java.lang.IllegalStateException

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

class AvailabilityConstraint : RequiredConstraint {
    override fun isSatisfied(schedule: Schedule): Boolean {
        TODO("Not yet implemented")
    }
}

class SlotsConstraint(slots: TimeSlots) : BooleanConstraint {

    private val sortedSlots = TimeSlots(
        slots.slots.sortedWith(
            compareBy({ it.second.units }, { it.first.units })
        )
    )

    init {
        // Validating that the slots don't intersect
        if (
            isIntersected(
                sortedSlots.slots.map {
                    Pair(it.second.units, it.first.units + it.second.units)
                }
            )
        ) {
            throw IllegalStateException("Slots should not intersect")
        }
    }

    override val weight: Double
        get() = TODO("Not yet implemented")

    override fun isSatisfied(schedule: Schedule): Boolean {
        fun getStartEnd(iSlots: Int): Pair<Int, Int> {
            return Pair(
                this.sortedSlots.slots[iSlots].second.units,
                this.sortedSlots.slots[iSlots].second.units +
                    this.sortedSlots.slots[iSlots].first.units
            )
        }

        val sortedSchedule = schedule.events.map {
            Pair(it.time.units, it.time.units + it.event.duration.units)
        }.sortedWith(
            compareBy({ it.first }, { it.second })
        )

        // Validating that the schedule don't intersect
        // TODO only one schedule per slot
        if (isIntersected(sortedSchedule)) {
            return false
        }

        var iSlots = 0
        for ((startEvent, endEvent) in sortedSchedule) {
            var isSatisfied = false
            while (iSlots < sortedSlots.slots.size) {
                val (startSlot, endSlots) = getStartEnd(iSlots)
                if (startSlot <= startEvent && endEvent <= endSlots) {
                    isSatisfied = true
                    break
                }
                iSlots++
            }
            if (!isSatisfied) {
                return false
            }
        }
        return true
    }

    private fun isIntersected(sortedIntervals: List<Pair<Int, Int>>): Boolean {
        var prevInterval: Pair<Int, Int>? = null
        for (interval in sortedIntervals) {
            if (prevInterval != null && interval.first <= prevInterval.second) {
                return true
            }
            prevInterval = interval
        }
        return false
    }
}
