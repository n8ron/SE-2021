package ru.hse.plameet.core.solvers

import ru.hse.plameet.core.Event
import ru.hse.plameet.core.Schedule
import ru.hse.plameet.core.constraints.Constraint

interface Solver {
    fun solve(events: List<Event>, constraints: List<Constraint>): Schedule
}
