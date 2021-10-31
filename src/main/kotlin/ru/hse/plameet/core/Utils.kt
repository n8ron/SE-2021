package ru.hse.plameet.core

import kotlin.math.max

fun intersectsSorted(sortedIntervals: Iterable<TimeRange>): Boolean {
    var prevInterval: TimeRange? = null
    for (interval in sortedIntervals) {
        if (prevInterval != null && interval.begin.units <= prevInterval.end.units) {
            return true
        }
        prevInterval = interval
    }
    return false
}

fun mergeIntervals(intervals: Collection<TimeRange>): Collection<TimeRange> {
    val sortedIntervals = intervals.sortedBy { it.begin.units }

    val result = mutableListOf<TimeRange>()
    for (timeRange in sortedIntervals) {
        if (result.size != 0) {
            val interval = result.last()
            if (interval.end.units < timeRange.begin.units) {
                result.add(timeRange)
                continue
            } else {
                result.removeLast()
                val intervalEnd = max(interval.end.units, timeRange.end.units)
                result.add(TimeRange(interval.begin, TimeStamp(intervalEnd)))
            }
        } else {
            result.add(timeRange)
        }
    }
    return result
}
