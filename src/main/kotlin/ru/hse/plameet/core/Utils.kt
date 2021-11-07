package ru.hse.plameet.core

import kotlin.math.max
import kotlin.math.min

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

fun intersect(intervalA: TimeRange, intervalB: TimeRange): TimeRange {
    return TimeRange(
        TimeStamp(max(intervalA.begin.units, intervalB.begin.units)),
        TimeStamp(min(intervalA.end.units, intervalB.end.units))
    )
}

fun intersectSorted(intervalsA: Iterable<TimeRange>, intervalsB: Iterable<TimeRange>): List<TimeRange> {
    val ans = mutableListOf<TimeRange>()
    val itA = intervalsA.iterator()
    val itB = intervalsB.iterator()
    var curA = itA.next()
    var curB = itB.next()
    while (true) {
        val intersection = intersect(curA, curB)
        if (!intersection.duration.empty()) {
            ans.add(intersection)
        }

        if (curA.begin.units < curB.begin.units) {
            if (itA.hasNext()) {
                curA = itA.next()
            } else {
                break
            }
        } else {
            if (itB.hasNext()) {
                curB = itB.next()
            } else {
                break
            }
        }
    }
    return ans
}

/**
 * Check if this range is a subrange of other range
 */
fun TimeRange.subrange(other: TimeRange): Boolean {
    return this.begin.units >= other.begin.units && this.end.units <= other.end.units
}

/**
 * Check if range is a subrange of any range in ranges (ranges sorted and does not intersects)
 */
fun TimeRange.subrangeSorted(ranges: List<TimeRange>): Boolean {
    var l = 0
    var r = ranges.size
    while (r - l > 1) {
        val m = (r + l) / 2
        if (ranges[m].begin.units <= this.begin.units) {
            l = m
        } else {
            r = m
        }
    }
    return this.subrange(ranges[l])
}
