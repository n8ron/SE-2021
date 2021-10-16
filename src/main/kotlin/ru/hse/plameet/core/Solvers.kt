package ru.hse.plameet.core

interface Solver {
    fun solve(events: List<Event>, constraints: List<Constraint>): Schedule
}
