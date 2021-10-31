package ru.hse.plameet.core.solvers

import ru.hse.plameet.core.Event
import ru.hse.plameet.core.Schedule
import ru.hse.plameet.core.constraints.Constraint

class MatchingSolver {
    companion object : Solver {
        override fun solve(events: List<Event>, constraints: List<Constraint>): Schedule {
            TODO("Not yet implemented")
        }
    }
}
