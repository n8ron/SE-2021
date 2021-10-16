package ru.hse.plameet.data

data class Event(val id: Int, val time: Pair<Int, Int>, val participants: List<Person>)
