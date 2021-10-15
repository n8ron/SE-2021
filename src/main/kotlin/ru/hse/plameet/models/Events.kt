package ru.hse.plameet.models

import java.time.Duration
import java.time.ZonedDateTime


interface Event {
    fun duration(): Duration
    fun start(): ZonedDateTime
    fun end(): ZonedDateTime
    fun participants(): List<Person>
    fun addParticipant(participant: Person): Boolean
    fun removeParticipant(participant: Person): Boolean
}

abstract class Meeting: Event
