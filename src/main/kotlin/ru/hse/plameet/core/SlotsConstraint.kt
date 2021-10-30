package ru.hse.plameet.core

import java.lang.IllegalStateException

open class SlotsConstraint(slots: TimeSlots) : BooleanConstraint {

    private val sortedSlots = TimeSlots(
        slots.slots.sortedWith(
            compareBy { it.begin.units }
        )
    )

    init {
        // Validating that the slots don't intersect
        if (intersectsSorted(sortedSlots.slots)) {
            throw IllegalStateException("Slots should not intersect")
        }
    }

    override val weight: Double
        get() = TODO("Not yet implemented")

    override fun isSatisfied(schedule: Schedule): Boolean {
        val sortedSchedule = schedule.events.asSequence()
            .map { it.time.during(it.event.duration) }
            .sortedWith(compareBy { it.begin.units })
            .toList()

        // Validating that the schedule don't intersect
        if (intersectsSorted(sortedSchedule)) {
            return false
        }

        var curSlot = 0
        for (event in sortedSchedule) {
            var isSatisfied = false
            while (curSlot < sortedSlots.slots.size) {
                val slot = sortedSlots.slots[curSlot++]
                if (slot.contains(event)) {
                    isSatisfied = true
                    break
                }
            }
            if (!isSatisfied) {
                return false
            }
        }

        return true
    }

    @JvmInline
    value class TimeSlots(val slots: List<TimeRange>)
}