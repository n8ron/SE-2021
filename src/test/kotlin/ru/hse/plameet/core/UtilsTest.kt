package ru.hse.plameet.core

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UtilsTest {

    @Test
    fun mergeIntervalsTest() {
        val expected2interval: List<Pair<List<TimeRange>, List<TimeRange>>> = mutableListOf(
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
            val mergedIntervals = mergeIntervals(it.second)
            assertTrue { it.first.containsAll(mergedIntervals) }
            assertTrue { mergedIntervals.containsAll(it.first) }
        }
    }

    @Test
    fun testSubrange() {
        assertTrue { timeRange(2, 4).subrange(timeRange(1, 5)) }
        assertTrue { timeRange(2, 4).subrange(timeRange(2, 5)) }
        assertTrue { timeRange(2, 4).subrange(timeRange(1, 4)) }
        assertTrue { timeRange(2, 4).subrange(timeRange(2, 4)) }
        assertFalse { timeRange(2, 4).subrange(timeRange(3, 14)) }
        assertFalse { timeRange(2, 4).subrange(timeRange(8, 14)) }
        assertFalse { timeRange(2, 4).subrange(timeRange(0, 2)) }
    }

    @Test
    fun testSubrangeSorted() {
        assertTrue { timeRange(5, 7).subrangeSorted(listOf(timeRange(1, 3), timeRange(4, 7), timeRange(9, 13))) }
        assertTrue { timeRange(5, 7).subrangeSorted(listOf(timeRange(2, 5), timeRange(5, 7), timeRange(9, 13))) }
        assertFalse { timeRange(5, 7).subrangeSorted(listOf(timeRange(2, 5), timeRange(5, 6), timeRange(7, 13))) }
    }
}
