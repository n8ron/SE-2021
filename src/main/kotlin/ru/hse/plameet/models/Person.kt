package ru.hse.plameet.models

import java.time.ZonedDateTime

interface Person {
    fun events(): List<Event>
    fun events(startDateTime: ZonedDateTime, endDateTime: ZonedDateTime): List<Event>
}