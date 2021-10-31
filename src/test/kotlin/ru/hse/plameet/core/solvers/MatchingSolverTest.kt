package ru.hse.plameet.core.solvers

import org.junit.jupiter.api.Test
import ru.hse.plameet.core.Duration
import ru.hse.plameet.core.Event
import ru.hse.plameet.core.TimeStamp
import ru.hse.plameet.core.constraints.RequiredConstraint
import ru.hse.plameet.core.constraints.SlotsConstraint
import ru.hse.plameet.core.during
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MatchingSolverTest {

    @Test
    fun slotsConstraintTest() {
        val oneSlot = SlotsConstraint(
            SlotsConstraint.TimeSlots(listOf(TimeStamp(0).during(Duration(5))))
        )
        val smallEvent = Event(0, Duration(2), listOf())
        checkSolver(listOf(smallEvent), listOf(oneSlot))
        checkSolver(listOf(smallEvent), listOf(oneSlot, oneSlot, oneSlot))
        assertNull(MatchingSolver.solve(listOf(smallEvent, smallEvent), listOf(oneSlot)))
        assertNull(MatchingSolver.solve(listOf(smallEvent, smallEvent), listOf(oneSlot, oneSlot, oneSlot)))

        val longEvent = Event(1, Duration(10), listOf())
        assertNull(MatchingSolver.solve(listOf(longEvent), listOf(oneSlot)))
        assertNull(MatchingSolver.solve(listOf(longEvent), listOf(oneSlot, oneSlot, oneSlot)))
        assertNull(MatchingSolver.solve(listOf(longEvent, longEvent), listOf(oneSlot)))

        val multipleSlots = SlotsConstraint(
            SlotsConstraint.TimeSlots(
                listOf(
                    TimeStamp(0).during(Duration(2)),
                    TimeStamp(5).during(Duration(4)),
                    TimeStamp(20).during(Duration(10))
                ),
            )
        )
        checkSolver(listOf(smallEvent), listOf(multipleSlots))
        checkSolver(listOf(smallEvent, smallEvent, smallEvent), listOf(multipleSlots))
        checkSolver(listOf(smallEvent, smallEvent, longEvent), listOf(multipleSlots))

        assertNull(MatchingSolver.solve(listOf(longEvent, longEvent), listOf(multipleSlots)))
        assertNull(MatchingSolver.solve(listOf(smallEvent, smallEvent, smallEvent, smallEvent), listOf(multipleSlots)))
        assertNull(MatchingSolver.solve(listOf(smallEvent, longEvent, longEvent), listOf(multipleSlots)))
    }

    private fun checkSolver(events: List<Event>, constraints: List<RequiredConstraint>) {
        val solution = MatchingSolver.solve(events, constraints)
        for (constraint in constraints) {
            assertTrue { constraint.isSatisfied(solution) }
        }
    }
}
