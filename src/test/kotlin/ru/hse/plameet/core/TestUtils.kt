package ru.hse.plameet.core

fun schedule(vararg timedEvent: TimedEvent): Schedule {
    return Schedule(timedEvent.toList())
}
