package ru.hse.plameet.core.constraints

import org.junit.jupiter.api.Test
import ru.hse.plameet.core.*
import kotlin.test.assertEquals

class UserPreferConstraintsTest {

    @Test
    fun preferConstraintsTest() {
        val user2penalty: Map<User, List<Pair<TimeRange, Double>>> = mapOf(
            User(0) to listOf(
                TimeStamp(0).until(TimeStamp(1)) to 10.0,
                TimeStamp(2).until(TimeStamp(6)) to 5.0,
                TimeStamp(7).until(TimeStamp(8)) to 6.0,
                TimeStamp(9).until(TimeStamp(10)) to 1.0,
            ),
            User(1) to listOf(
                TimeStamp(0).until(TimeStamp(1)) to 7.0,
                TimeStamp(2).until(TimeStamp(3)) to 1.0,
                TimeStamp(4).until(TimeStamp(6)) to 5.0,
                TimeStamp(7).until(TimeStamp(10)) to 11.0,
            ),
            User(2) to listOf(
                TimeStamp(0).until(TimeStamp(5)) to 3.0,
                TimeStamp(6).until(TimeStamp(7)) to 2.0,
                TimeStamp(8).until(TimeStamp(10)) to 10.0,
            )
        )

        val constraint = UserPreferConstraints(user2penalty)

        val events = listOf(
            Event(0, Duration(1), listOf(User(0))),
            Event(1, Duration(3), listOf(User(1))),
            Event(2, Duration(2), listOf(User(2))),
            Event(3, Duration(3), listOf(User(0), User(1))),
            Event(4, Duration(1), listOf(User(2), User(1))),
            Event(5, Duration(2), listOf(User(2), User(0))),
            Event(6, Duration(10), listOf(User(2), User(1), User(0)))
        )

        var schedule = schedule(
            TimedEvent(events[6], TimeStamp(0))
        )

        assertEquals(61.0, constraint.calcPenalty(schedule))

        schedule = schedule(
            TimedEvent(events[0], TimeStamp(9)),
            TimedEvent(events[1], TimeStamp(1)),
            TimedEvent(events[2], TimeStamp(5)),
        )

        assertEquals(19.0, constraint.calcPenalty(schedule))

        schedule = schedule(
            TimedEvent(events[3], TimeStamp(5)),
            TimedEvent(events[4], TimeStamp(1)),
            TimedEvent(events[0], TimeStamp(3)),
        )

        assertEquals(43.0, constraint.calcPenalty(schedule))

        schedule = schedule(
            TimedEvent(events[5], TimeStamp(4)),
            TimedEvent(events[0], TimeStamp(7)),
            TimedEvent(events[2], TimeStamp(1)),
        )

        assertEquals(19.0, constraint.calcPenalty(schedule))
    }
}
