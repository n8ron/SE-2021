package ru.hse.plameet.core

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TimetableConstraintTest {

    @Test
    fun timetableConstraintTest() {
        val constraint = TimetableConstraint(
            listOf(0, 5, 10, 20, 25, 41).map { TimeStamp(it) },
            Duration(3)
        )

        var correctSchedule = Schedule(listOf(TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(0))))
        assertTrue { constraint.isSatisfied(correctSchedule) }

        correctSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(5)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(20)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(41)),
            )
        )
        assertTrue { constraint.isSatisfied(correctSchedule) }

        correctSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(5)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(10)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(20)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(25)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(41)),
            )
        )
        assertTrue { constraint.isSatisfied(correctSchedule) }

        correctSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(2)),
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(6)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(11)),
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(20)),
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(26)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(43)),
            )
        )
        assertTrue { constraint.isSatisfied(correctSchedule) }

        var incorrectSchedule = Schedule(listOf(TimedEvent(Event(0, Duration(4), listOf()), TimeStamp(0))))
        assertFalse { constraint.isSatisfied(incorrectSchedule) }

        incorrectSchedule = Schedule(listOf(TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(3))))
        assertFalse { constraint.isSatisfied(incorrectSchedule) }

        incorrectSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(1)),
            )
        )
        assertFalse { constraint.isSatisfied(incorrectSchedule) }

        incorrectSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(2)),
            )
        )
        assertFalse { constraint.isSatisfied(incorrectSchedule) }

        incorrectSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(5)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(10)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(15)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(20)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(25)),
            )
        )
        assertFalse { constraint.isSatisfied(incorrectSchedule) }

        incorrectSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(5)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(10)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(20)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(20)),
            )
        )
        assertFalse { constraint.isSatisfied(incorrectSchedule) }

        incorrectSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(5)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(10)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(20)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(25)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(41)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(41)),
            )
        )
        assertFalse { constraint.isSatisfied(incorrectSchedule) }

        assertThrows<IllegalStateException> {
            TimetableConstraint(
                listOf(0, 5, 10, 13).map { TimeStamp(it) },
                Duration(3)
            )
        }

        assertThrows<IllegalStateException> {
            TimetableConstraint(
                listOf(0, 2).map { TimeStamp(it) },
                Duration(5)
            )
        }

        assertThrows<IllegalStateException> {
            TimetableConstraint(
                listOf(0, 0).map { TimeStamp(it) },
                Duration(5)
            )
        }

        assertThrows<IllegalStateException> {
            TimetableConstraint(
                listOf(0, 11, 23, 9).map { TimeStamp(it) },
                Duration(10)
            )
        }
    }
}