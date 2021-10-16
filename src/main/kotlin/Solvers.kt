
interface Solver {
    fun solve(events: List<Event>, constraints: List<Constraint>): Schedule
}

class MatchingSolver : Solver {
    override fun solve(events: List<Event>, constraints: List<Constraint>): Schedule {
        TODO("Not yet implemented")
    }
}

class FlowSolver : Solver {
    override fun solve(events: List<Event>, constraints: List<Constraint>): Schedule {
        TODO("Not yet implemented")
    }
}

class BruteForceSolver : Solver {
    override fun solve(events: List<Event>, constraints: List<Constraint>): Schedule {
        TODO("Not yet implemented")
    }
}