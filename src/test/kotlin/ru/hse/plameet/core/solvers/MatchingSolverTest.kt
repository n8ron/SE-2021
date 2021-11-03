package ru.hse.plameet.core.solvers

import org.junit.jupiter.api.Test
import ru.hse.plameet.core.Duration
import ru.hse.plameet.core.Event
import ru.hse.plameet.core.TimeStamp
import ru.hse.plameet.core.constraints.AllInConstraint
import ru.hse.plameet.core.constraints.RequiredConstraint
import ru.hse.plameet.core.constraints.SlotsConstraint
import ru.hse.plameet.core.during
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MatchingSolverTest {

    @Test
    fun slotsConstraintTest() {
        var id = 0
        val smallEvent = { Event(id++, Duration(2), listOf()) }
        val longEvent = { Event(id++, Duration(10), listOf()) }

        val oneSlot = SlotsConstraint(
            listOf(TimeStamp(0).during(Duration(5)))
        )
        checkSolver(listOf(smallEvent()), listOf(oneSlot), true)
        checkSolver(listOf(smallEvent(), smallEvent()), listOf(oneSlot), false)

        checkSolver(listOf(longEvent()), listOf(oneSlot), false)
        checkSolver(listOf(longEvent(), longEvent()), listOf(oneSlot), false)

        val multipleSlots = SlotsConstraint(
            listOf(
                TimeStamp(0).during(Duration(2)),
                TimeStamp(5).during(Duration(4)),
                TimeStamp(20).during(Duration(10))
            )
        )
        checkSolver(listOf(smallEvent()), listOf(multipleSlots), true)
        checkSolver(listOf(smallEvent(), smallEvent(), smallEvent()), listOf(multipleSlots), true)
        checkSolver(listOf(smallEvent(), smallEvent(), longEvent()), listOf(multipleSlots), true)

        checkSolver(listOf(longEvent(), longEvent()), listOf(multipleSlots), false)
        checkSolver(listOf(smallEvent(), smallEvent(), smallEvent(), smallEvent()), listOf(multipleSlots), false)
        checkSolver(listOf(smallEvent(), longEvent(), longEvent()), listOf(multipleSlots), false)
    }

    @Test
    fun multipleSlotsConstraintTest() {
        var id = 0
        val smallEvent = { Event(id++, Duration(2), listOf()) }
        val longEvent = { Event(id++, Duration(10), listOf()) }

        val slotsConstraints = listOf(
            SlotsConstraint( // 0
                listOf(
                    TimeStamp(0).during(Duration(10)),
                    TimeStamp(20).during(Duration(10))
                )
            ),
            SlotsConstraint( // 1
                listOf(
                    TimeStamp(0).during(Duration(2)),
                    TimeStamp(5).during(Duration(4)),
                    TimeStamp(20).during(Duration(15))
                )
            ),
            SlotsConstraint( // 2
                listOf(
                    TimeStamp(6).during(Duration(5)),
                    TimeStamp(22).during(Duration(5))
                )
            ),
            SlotsConstraint( // 3
                listOf(
                    TimeStamp(0).during(Duration(8)),
                    TimeStamp(24).during(Duration(10))
                )
            ),
            SlotsConstraint( // 4
                listOf(
                    TimeStamp(2).during(Duration(3)),
                    TimeStamp(9).during(Duration(11))
                )
            )
        )
        checkSolver(listOf(smallEvent()), listOf(slotsConstraints[0], slotsConstraints[4]), true)
        checkSolver(listOf(smallEvent(), smallEvent()), listOf(slotsConstraints[0], slotsConstraints[4]), false)
        checkSolver(listOf(longEvent()), listOf(slotsConstraints[0], slotsConstraints[4]), false)

        checkSolver(
            listOf(smallEvent(), longEvent(), longEvent()),
            listOf(slotsConstraints[0], slotsConstraints[1]),
            false
        )

        checkSolver(listOf(smallEvent(), smallEvent()), listOf(slotsConstraints[1], slotsConstraints[2]), true)
        checkSolver(
            listOf(smallEvent(), smallEvent(), smallEvent()),
            listOf(slotsConstraints[1], slotsConstraints[2]),
            false
        )
        checkSolver(listOf(longEvent()), listOf(slotsConstraints[1], slotsConstraints[2]), false)

        checkSolver(listOf(smallEvent(), smallEvent()), listOf(slotsConstraints[1], slotsConstraints[3]), true)
        checkSolver(
            listOf(smallEvent(), smallEvent(), smallEvent()),
            listOf(slotsConstraints[1], slotsConstraints[3]),
            false
        )
        checkSolver(listOf(smallEvent(), longEvent()), listOf(slotsConstraints[1], slotsConstraints[3]), true)

        checkSolver(
            listOf(smallEvent(), smallEvent()),
            listOf(slotsConstraints[1], slotsConstraints[2], slotsConstraints[3]),
            true
        )
        checkSolver(
            listOf(smallEvent(), smallEvent(), smallEvent()),
            listOf(slotsConstraints[1], slotsConstraints[2], slotsConstraints[3]),
            false
        )
        checkSolver(listOf(longEvent()), listOf(slotsConstraints[1], slotsConstraints[2], slotsConstraints[3]), false)

        checkSolver(listOf(smallEvent()), listOf(slotsConstraints[1], slotsConstraints[4]), false)
        checkSolver(listOf(longEvent()), listOf(slotsConstraints[1], slotsConstraints[4]), false)

        checkSolver(listOf(smallEvent()), listOf(slotsConstraints[3], slotsConstraints[4]), true)
        checkSolver(listOf(smallEvent(), smallEvent()), listOf(slotsConstraints[3], slotsConstraints[4]), false)
        checkSolver(listOf(longEvent()), listOf(slotsConstraints[3], slotsConstraints[4]), false)
    }

    @Test
    fun allEventsTest() {
        var id = 0
        fun smallEvents() = sequence {
            while (true) {
                yield(Event(id++, Duration(2), listOf()))
            }
        }

        fun longEvents() = sequence {
            while (true) {
                yield(Event(id++, Duration(10), listOf()))
            }
        }

        val slotsConstraint = listOf(
            SlotsConstraint(
                listOf(
                    TimeStamp(0).during(Duration(2)),
                    TimeStamp(5).during(Duration(4)),
                    TimeStamp(20).during(Duration(15))
                )
            )
        )
        checkSolver(smallEvents().take(3).toList(), slotsConstraint, true)
        checkSolver(longEvents().take(1).toList(), slotsConstraint, true)

        checkSolver(smallEvents().take(4).toList(), slotsConstraint, false)
        checkSolver(longEvents().take(2).toList(), slotsConstraint, false)
    }

    private fun checkSolver(
        events: List<Event>,
        constraints: List<RequiredConstraint>,
        solvable: Boolean
    ) {
        val allInConstraint = AllInConstraint(events)
        val solution = MatchingSolver.solve(events, constraints + allInConstraint)
        if (solvable) {
            for (constraint in constraints) {
                assertNotNull(solution)
                assertTrue { constraint.isSatisfied(solution) }
                assertTrue { allInConstraint.isSatisfied(solution) }
            }
        } else {
            assertNull(solution)
        }
    }
}