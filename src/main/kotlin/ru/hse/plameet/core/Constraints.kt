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
        // TODO Validate sortedSlots should be non-intersection
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
            compareBy({ it.first}, {it.second})
        )

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
}

