package ru.hse.plameet.core

fun schedule(vararg timedEvent: TimedEvent) = Schedule(timedEvent.toList())

fun timeRange(begin: Int, end: Int) = TimeRange(TimeStamp(begin), TimeStamp(end))
