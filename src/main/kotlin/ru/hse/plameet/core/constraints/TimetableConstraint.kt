package ru.hse.plameet.core.constraints

import ru.hse.plameet.core.Duration
import ru.hse.plameet.core.TimeStamp
import ru.hse.plameet.core.during

open class TimetableConstraint(slots: List<TimeStamp>, duration: Duration) :
    SlotsConstraint(TimeSlots(slots.map { it.during(duration) }))
