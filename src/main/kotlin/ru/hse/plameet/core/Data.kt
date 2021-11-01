package ru.hse.plameet.core

@JvmInline
value class User(val id: Int)

@JvmInline
value class Duration(val units: Int)

@JvmInline
value class TimeStamp(val units: Int)

data class TimeRange(val begin: TimeStamp, val end: TimeStamp) {
    val duration: Duration
        get() = end - begin
}

data class Event(val id: Int, val duration: Duration, val participants: List<User>)

data class TimedEvent(val event: Event, val time: TimeStamp)

data class Schedule(val events: List<TimedEvent>)

operator fun TimeStamp.plus(duration: Duration) = TimeStamp(this.units + duration.units)
operator fun TimeStamp.minus(timeStamp: TimeStamp) = Duration(this.units - timeStamp.units)

fun Duration.empty() = this.units <= 0

fun TimeStamp.until(end: TimeStamp) = TimeRange(this, end)
fun TimeStamp.during(duration: Duration) = TimeRange(this, this + duration)

fun TimeRange.contains(other: TimeRange) = this.begin.units <= other.begin.units && other.end.units <= this.end.units
