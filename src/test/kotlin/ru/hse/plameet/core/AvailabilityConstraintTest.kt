package ru.hse.plameet.core

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

object AvailabilityConstraintTest {
    @ParameterizedTest
    @MethodSource("provideCorrect")
    fun testCorrect(av: Map<User, List<TimeRange>>, schedules: List<Schedule>) {
        val constraint = AvailabilityConstraint(av)
        schedules.forEach { assertTrue(constraint.isSatisfied(it)) }
    }

    @JvmStatic
    private fun provideCorrect(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(
                mapOf(
                    User(1) to listOf(
                        TimeStamp(0).until(TimeStamp(4)),
                        TimeStamp(5).until(TimeStamp(8))
                    ),
                    User(2) to listOf(
                        TimeStamp(0).until(TimeStamp(1)),
                        TimeStamp(3).until(TimeStamp(8))
                    )
                ),
                listOf(
                    schedule(
                        TimedEvent(Event(0, Duration(1), listOf(User(1), User(2))), TimeStamp(5)),
                        TimedEvent(Event(1, Duration(1), listOf(User(1))), TimeStamp(1))
                    )
                )
            )
        )
    }

    @ParameterizedTest
    @MethodSource("provideIncorrect")
    fun testIncorrect(av: Map<User, List<TimeRange>>, schedules: List<Schedule>) {
        val constraint = AvailabilityConstraint(av)
        schedules.forEach { assertFalse(constraint.isSatisfied(it)) }
    }

    @JvmStatic
    private fun provideIncorrect(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(
                mapOf(
                    User(1) to listOf(
                        TimeStamp(0).until(TimeStamp(4)),
                        TimeStamp(5).until(TimeStamp(8))
                    ),
                    User(2) to listOf(
                        TimeStamp(0).until(TimeStamp(1)),
                        TimeStamp(3).until(TimeStamp(8))
                    )
                ),
                listOf(
                    schedule(
                        TimedEvent(Event(0, Duration(1), listOf(User(1), User(2))), TimeStamp(6)),
                        TimedEvent(Event(1, Duration(3), listOf(User(1))), TimeStamp(5))
                    ),
                    schedule(
                        TimedEvent(Event(0, Duration(1), listOf(User(1), User(2))), TimeStamp(4))
                    ),
                    schedule(
                        TimedEvent(Event(0, Duration(1), listOf(User(1), User(2))), TimeStamp(0)),
                        TimedEvent(Event(0, Duration(1), listOf(User(1))), TimeStamp(4))
                    )
                )
            )
        )
    }
}