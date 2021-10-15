package ru.hse.plameet.algorithms

import ru.hse.plameet.models.Person
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

interface TimeIntervalFinder {

    fun findIntervalTimeInterval(persons: List<Person>): List<Pair<ZonedDateTime, ZonedDateTime>>

    fun findIntervalTimeInterval(
        person1: Person,
        person2: Person
    ): List<Pair<ZonedDateTime, ZonedDateTime>>

    fun findIntervalTimeInterval(
        persons: List<Person>,
        intervalForEvent: Pair<ZonedDateTime, ZonedDateTime>
    ): List<Pair<ZonedDateTime, ZonedDateTime>>

    fun findIntervalTimeInterval(
        persons: List<Person>,
        day: LocalDate,
        zoneId: ZoneId
    ): List<Pair<ZonedDateTime, ZonedDateTime>>

}