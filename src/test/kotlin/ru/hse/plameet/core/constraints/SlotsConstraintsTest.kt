package ru.hse.plameet.core.constraints

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import ru.hse.plameet.core.*
import ru.hse.plameet.core.constraints.SlotsConstraint.TimeSlots
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SlotsConstraintsTest {

    @Test
    fun slotsConstraintIsSatisfiedTest() {
        val slots = TimeSlots(
            listOf(
                TimeStamp(0).during(Duration(2)),
                TimeStamp(3).during(Duration(1)),
                TimeStamp(5).during(Duration(4)),
                TimeStamp(10).during(Duration(1))
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
                TimedEvent(Event(0, Duration(4), listOf()), TimeStamp(5)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(10)),
            )
        )
        assertTrue { constraint.isSatisfied(correctSchedule) }

        correctSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(1)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(3)),
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(6)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(10)),
            )
        )
        assertTrue { constraint.isSatisfied(correctSchedule) }

        correctSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(1)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(3)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(5)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(10)),
            )
        )
        assertTrue { constraint.isSatisfied(correctSchedule) }
    }

    @Test
    fun slotsConstraintScheduleIntersectionsTest() {
        val slots = TimeSlots(
            listOf(
                TimeStamp(0).during(Duration(2)),
                TimeStamp(3).during(Duration(1)),
                TimeStamp(5).during(Duration(4)),
                TimeStamp(10).during(Duration(1))
            )
        )

        val constraint = SlotsConstraint(slots)

        var scheduleWithIntersections = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(0)),
            )
        )
        assertFalse { constraint.isSatisfied(scheduleWithIntersections) }

        scheduleWithIntersections = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(1)),
            )
        )
        assertFalse { constraint.isSatisfied(scheduleWithIntersections) }

        scheduleWithIntersections = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(0)),
            )
        )
        assertFalse { constraint.isSatisfied(scheduleWithIntersections) }

        scheduleWithIntersections = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(5)),
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(6)),
            )
        )
        assertFalse { constraint.isSatisfied(scheduleWithIntersections) }
    }

    @Test
    fun slotsConstraintSlotIntersectionsTest() {
        var intersectSlots = TimeSlots(
            listOf(
                TimeStamp(0).during(Duration(2)),
                TimeStamp(2).during(Duration(1)),
            )
        )
        assertThrows<IllegalStateException> { SlotsConstraint(intersectSlots) }

        intersectSlots = TimeSlots(
            listOf(
                TimeStamp(0).during(Duration(2)),
                TimeStamp(4).during(Duration(4)),
                TimeStamp(7).during(Duration(1)),
                TimeStamp(3).during(Duration(1))
            )
        )
        assertThrows<IllegalStateException> { SlotsConstraint(intersectSlots) }

        intersectSlots = TimeSlots(
            listOf(
                TimeStamp(0).during(Duration(2)),
                TimeStamp(4).during(Duration(4)),
                TimeStamp(5).during(Duration(1)),
                TimeStamp(3).during(Duration(1))
            )
        )
        assertThrows<IllegalStateException> { SlotsConstraint(intersectSlots) }

        val correctSlots = TimeSlots(
            listOf(
                TimeStamp(0).during(Duration(2)),
                TimeStamp(3).during(Duration(5)),
                TimeStamp(9).during(Duration(1)),
                TimeStamp(11).during(Duration(2)),
            )
        )

        assertDoesNotThrow { SlotsConstraint(correctSlots) }
    }

    @Test
    fun slotConstraintManyEventInOneSlotTest() {
        val slots = TimeSlots(
            listOf(
                TimeStamp(0).during(Duration(3)),
                TimeStamp(4).during(Duration(2)),
                TimeStamp(8).during(Duration(4)),
                TimeStamp(13).during(Duration(1))
            )
        )

        val constraint = SlotsConstraint(slots)

        var incorrectSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(8)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(11)),
            )
        )

        assertFalse { constraint.isSatisfied(incorrectSchedule) }

        val correctSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(4), listOf()), TimeStamp(8)),
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(4)),
                TimedEvent(Event(0, Duration(3), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(13))
            )
        )

        assertTrue { constraint.isSatisfied(correctSchedule) }

        incorrectSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(4), listOf()), TimeStamp(8)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(2)),
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(4)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(0)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(13))
            )
        )

        assertFalse { constraint.isSatisfied(incorrectSchedule) }
    }

    @Test
    fun slotsConstraintCalcPenaltyTest() {
        val slots = TimeSlots(
            listOf(
                TimeStamp(4).during(Duration(3)),
                TimeStamp(9).during(Duration(4)),
                TimeStamp(14).during(Duration(1)),
                TimeStamp(17).during(Duration(2))
            )
        )

        val constraint = SlotsConstraint(slots)

        val correctSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(5)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(14))
            )
        )

        assertEquals(.0, constraint.calcPenalty(correctSchedule))

        val incorrectSchedule = Schedule(
            listOf(
                TimedEvent(Event(0, Duration(2), listOf()), TimeStamp(9)),
                TimedEvent(Event(0, Duration(1), listOf()), TimeStamp(11))
            )
        )

        assertEquals(Double.MAX_VALUE, constraint.calcPenalty(incorrectSchedule))
    }
}
