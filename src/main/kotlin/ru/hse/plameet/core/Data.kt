package ru.hse.plameet.core

@JvmInline
value class User(val id: Int)

@JvmInline
value class Duration(val units: Int)

@JvmInline
value class TimeStamp(val units: Int)

data class Event(val id: Int, val duration: Duration, val participants: List<User>)

data class TimedEvent(val event: Event, val time: TimeStamp)

data class Schedule(val events: List<TimedEvent>)
