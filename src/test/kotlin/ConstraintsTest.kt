package ru.hse.plameet.core

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class ConstraintsTest {

    @Test
    fun slotsConstraintTest() {
        val slots = TimeSlots(
            listOf(
                Pair(Duration(2), TimeStamp(0)),
                Pair(Duration(1), TimeStamp(3)),
                Pair(Duration(4), TimeStamp(4)),
                Pair(Duration(1), TimeStamp(9))
            )
        )

        val constraint = SlotsConstraint(slots)

        val emptySchedule = Schedule(listOf())
        assertTrue { constraint.isSatisfied(emptySchedule) }

        val lateEvent = Schedule(listOf(TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(20))))
        assertFalse { constraint.isSatisfied(lateEvent) }

        val longEvent = Schedule(listOf(TimedEvent(Event(0, Duration(10), listOf()), TimeStamp(0))))
        assertFalse { constraint.isSatisfied(longEvent) }

        var correctSchedule = Schedule(listOf(TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(0))))
        assertTrue { constraint.isSatisfied(correctSchedule) }

        correctSchedule = Schedule(listOf(TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(1))))
        assertTrue { constraint.isSatisfied(correctSchedule) }

        correctSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(3)),
                TimedEvent(Event(0, Duration(4), listOf()), TimeStamp(4)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(9)),
            )
        )
        assertTrue { constraint.isSatisfied(correctSchedule) }

        correctSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(1)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(3)),
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(5)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(9)),
            )
        )
        assertTrue { constraint.isSatisfied(correctSchedule) }
    }

    @Test
    fun slotsConstraintIntersectionsTest() {
        var intersectSlots = TimeSlots(
            listOf(
                Pair(Duration(2), TimeStamp(0)),
                Pair(Duration(1), TimeStamp(2)),
            )
        )
        assertThrows<IllegalStateException> { SlotsConstraint(intersectSlots)}

        intersectSlots = TimeSlots(
            listOf(
                Pair(Duration(2), TimeStamp(0)),
                Pair(Duration(4), TimeStamp(4)),
                Pair(Duration(1), TimeStamp(7)),
                Pair(Duration(1), TimeStamp(3))
            )
        )
        assertThrows<IllegalStateException> { SlotsConstraint(intersectSlots)}

        intersectSlots = TimeSlots(
            listOf(
                Pair(Duration(2), TimeStamp(0)),
                Pair(Duration(4), TimeStamp(4)),
                Pair(Duration(1), TimeStamp(5)),
                Pair(Duration(1), TimeStamp(3))
            )
        )
        assertThrows<IllegalStateException> { SlotsConstraint(intersectSlots)}

        val correctSlots = TimeSlots(
            listOf(
                Pair(Duration(2), TimeStamp(0)),
                Pair(Duration(5), TimeStamp(3)),
                Pair(Duration(1), TimeStamp(9)),
                Pair(Duration(2), TimeStamp(11)),
            )
        )

        assertDoesNotThrow {SlotsConstraint(correctSlots)}
    }

}