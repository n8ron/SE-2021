package ru.hse.plameet.core.constraints

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.hse.plameet.core.*
import java.util.stream.Stream

object EventsRequiredConstraintTest {

    @ParameterizedTest
    @MethodSource("provideCorrect")
    fun testCorrect(events: List<Event>, schedule: Schedule) {
        assertTrue(EventsRequiredConstraint(events).isSatisfied(schedule))
    }

    @JvmStatic
    private fun provideCorrect(): Stream<Arguments> {
        val events = listOf(
            Event(1, Duration(1), listOf()),
            Event(2, Duration(2), listOf()),
            Event(3, Duration(9), listOf())
        )
        return Stream.of(
            Arguments.of(
                events,
                schedule(
                    TimedEvent(events[0], TimeStamp(2)),
                    TimedEvent(events[1], TimeStamp(2)),
                    TimedEvent(events[2], TimeStamp(2)),
                )
            ),
            Arguments.of(
                events,
                schedule(
                    TimedEvent(events[1], TimeStamp(10)),
                    TimedEvent(events[0], TimeStamp(1)),
                    TimedEvent(events[2], TimeStamp(20)),
                )
            ),
            Arguments.of(
                events,
                schedule(
                    TimedEvent(events[2], TimeStamp(0)),
                    TimedEvent(events[1], TimeStamp(0)),
                    TimedEvent(events[0], TimeStamp(0)),
                )
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("provideIncorrect")
    fun testIncorrect(events: List<Event>, schedule: Schedule) {
        assertFalse(EventsRequiredConstraint(events).isSatisfied(schedule))
    }

    @JvmStatic
    private fun provideIncorrect(): Stream<Arguments> {
        val events = listOf(
            Event(1, Duration(1), listOf()),
            Event(2, Duration(2), listOf()),
            Event(3, Duration(9), listOf())
        )
        return Stream.of(
            Arguments.of(
                events,
                schedule(
                    TimedEvent(events[0], TimeStamp(2)),
                    TimedEvent(events[1], TimeStamp(6)),
                )
            ),
            Arguments.of(
                events,
                schedule(
                    TimedEvent(events[1], TimeStamp(1)),
                    TimedEvent(events[0], TimeStamp(10)),
                    TimedEvent(Event(4, Duration(1), listOf()), TimeStamp(2)),
                )
            ),
            Arguments.of(
                events,
                schedule(
                    TimedEvent(events[2], TimeStamp(0)),
                    TimedEvent(events[1], TimeStamp(0)),
                    TimedEvent(events[2], TimeStamp(0)),
                )
            ),
        )
    }
}
