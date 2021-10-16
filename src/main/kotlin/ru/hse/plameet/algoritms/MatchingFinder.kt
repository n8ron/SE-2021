package ru.hse.plameet.algoritms

import ru.hse.plameet.data.Person
import ru.hse.plameet.data.Schedule

interface MatchingFinder {
    fun findCommonFreeTime(persons: List<Person>) : Schedule

    fun findScheduleOnePerson(person: Person, companies: List<Person>) : Schedule
}
