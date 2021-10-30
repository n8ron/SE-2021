package ru.hse.plameet.core

open class TimetableConstraint(slots: List<TimeStamp>, duration: Duration) :
    SlotsConstraint(TimeSlots(slots.map { it.during(duration) }))
