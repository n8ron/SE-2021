package ru.hse.plameet.core

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class UtilsTest {

    @Test
    fun mergeIntervalsTest() {
        val expected2interval: List<Pair<List<TimeRange>, List<TimeRange>>> = listOf(
            listOf(
                TimeRange(TimeStamp(0), TimeStamp(2)),
                TimeRange(TimeStamp(3), TimeStamp(6)),
                TimeRange(TimeStamp(7), TimeStamp(10))
            ) to listOf(
                TimeRange(TimeStamp(0), TimeStamp(1)),
                TimeRange(TimeStamp(5), TimeStamp(6)),
                TimeRange(TimeStamp(1), TimeStamp(2)),
                TimeRange(TimeStamp(7), TimeStamp(10)),
                TimeRange(TimeStamp(3), TimeStamp(5)),
            ),
            listOf(
                TimeRange(TimeStamp(0), TimeStamp(2)),
                TimeRange(TimeStamp(3), TimeStamp(6)),
                TimeRange(TimeStamp(7), TimeStamp(10))
            ) to listOf(
                TimeRange(TimeStamp(0), TimeStamp(2)),
                TimeRange(TimeStamp(4), TimeStamp(6)),
                TimeRange(TimeStamp(1), TimeStamp(2)),
                TimeRange(TimeStamp(8), TimeStamp(9)),
                TimeRange(TimeStamp(7), TimeStamp(10)),
                TimeRange(TimeStamp(3), TimeStamp(5)),
            ),
            listOf(
                TimeRange(TimeStamp(0), TimeStamp(2))
            ) to listOf(
                TimeRange(TimeStamp(0), TimeStamp(2)),
                TimeRange(TimeStamp(0), TimeStamp(2)),
                TimeRange(TimeStamp(1), TimeStamp(2)),
                TimeRange(TimeStamp(0), TimeStamp(2))
            ),
            listOf<TimeRange>() to listOf()
        )
        expected2interval.forEach {
            assertTrue(
                it.first.containsAll(it.second) && it.second.containsAll(it.first)
            )
        }
    }
}
