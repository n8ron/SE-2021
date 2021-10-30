package ru.hse.plameet.core

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

fun mergeIntervals(intervals: Iterable<TimeRange>): Iterable<TimeRange> {
    TODO("Not implemented")
}
