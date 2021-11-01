package ru.hse.plameet.core.constraints

import ru.hse.plameet.core.*

open class SlotsConstraint(slots: List<TimeRange>) : RequiredConstraint {

    internal val sortedSlots =
        slots.sortedWith(
            compareBy { it.begin.units }
        )

    init {
        // Validating that the slots don't intersect
        if (intersectsSorted(sortedSlots)) {
            throw IllegalStateException("Slots should not intersect")
        }
    }

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
            while (curSlot < sortedSlots.size) {
                val slot = sortedSlots[curSlot++]
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
}
