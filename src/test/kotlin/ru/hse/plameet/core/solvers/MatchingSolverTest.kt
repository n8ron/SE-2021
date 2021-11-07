package ru.hse.plameet.core.solvers

import org.junit.jupiter.api.Test
import ru.hse.plameet.core.Duration
import ru.hse.plameet.core.Event
import ru.hse.plameet.core.TimeStamp
import ru.hse.plameet.core.constraints.EventsRequiredConstraint
import ru.hse.plameet.core.constraints.RequiredConstraint
import ru.hse.plameet.core.constraints.TimeSlotsConstraint
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

        val oneSlot = TimeSlotsConstraint(
            listOf(TimeStamp(0).during(Duration(5)))
        )
        checkSolver(listOf(smallEvent()), listOf(oneSlot), true)
        checkSolver(listOf(smallEvent(), smallEvent()), listOf(oneSlot), false)

        checkSolver(listOf(longEvent()), listOf(oneSlot), false)
        checkSolver(listOf(longEvent(), longEvent()), listOf(oneSlot), false)

        val multipleSlots = TimeSlotsConstraint(
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

        val timeSlotsConstraints = listOf(
            TimeSlotsConstraint( // 0
                listOf(
                    TimeStamp(0).during(Duration(10)),
                    TimeStamp(20).during(Duration(10))
                )
            ),
            TimeSlotsConstraint( // 1
                listOf(
                    TimeStamp(0).during(Duration(2)),
                    TimeStamp(5).during(Duration(4)),
                    TimeStamp(20).during(Duration(15))
                )
            ),
            TimeSlotsConstraint( // 2
                listOf(
                    TimeStamp(6).during(Duration(5)),
                    TimeStamp(22).during(Duration(5))
                )
            ),
            TimeSlotsConstraint( // 3
                listOf(
                    TimeStamp(0).during(Duration(8)),
                    TimeStamp(24).during(Duration(10))
                )
            ),
            TimeSlotsConstraint( // 4
                listOf(
                    TimeStamp(2).during(Duration(3)),
                    TimeStamp(9).during(Duration(11))
                )
            )
        )
        checkSolver(listOf(smallEvent()), listOf(timeSlotsConstraints[0], timeSlotsConstraints[4]), true)
        checkSolver(listOf(smallEvent(), smallEvent()), listOf(timeSlotsConstraints[0], timeSlotsConstraints[4]), false)
        checkSolver(listOf(longEvent()), listOf(timeSlotsConstraints[0], timeSlotsConstraints[4]), false)

        checkSolver(
            listOf(smallEvent(), longEvent(), longEvent()),
            listOf(timeSlotsConstraints[0], timeSlotsConstraints[1]),
            false
        )

        checkSolver(listOf(smallEvent(), smallEvent()), listOf(timeSlotsConstraints[1], timeSlotsConstraints[2]), true)
        checkSolver(
            listOf(smallEvent(), smallEvent(), smallEvent()),
            listOf(timeSlotsConstraints[1], timeSlotsConstraints[2]),
            false
        )
        checkSolver(listOf(longEvent()), listOf(timeSlotsConstraints[1], timeSlotsConstraints[2]), false)

        checkSolver(listOf(smallEvent(), smallEvent()), listOf(timeSlotsConstraints[1], timeSlotsConstraints[3]), true)
        checkSolver(
            listOf(smallEvent(), smallEvent(), smallEvent()),
            listOf(timeSlotsConstraints[1], timeSlotsConstraints[3]),
            false
        )
        checkSolver(listOf(smallEvent(), longEvent()), listOf(timeSlotsConstraints[1], timeSlotsConstraints[3]), true)

        checkSolver(
            listOf(smallEvent(), smallEvent()),
            listOf(timeSlotsConstraints[1], timeSlotsConstraints[2], timeSlotsConstraints[3]),
            true
        )
        checkSolver(
            listOf(smallEvent(), smallEvent(), smallEvent()),
            listOf(timeSlotsConstraints[1], timeSlotsConstraints[2], timeSlotsConstraints[3]),
            false
        )
        checkSolver(listOf(longEvent()), listOf(timeSlotsConstraints[1], timeSlotsConstraints[2], timeSlotsConstraints[3]), false)

        checkSolver(listOf(smallEvent()), listOf(timeSlotsConstraints[1], timeSlotsConstraints[4]), false)
        checkSolver(listOf(longEvent()), listOf(timeSlotsConstraints[1], timeSlotsConstraints[4]), false)

        checkSolver(listOf(smallEvent()), listOf(timeSlotsConstraints[3], timeSlotsConstraints[4]), true)
        checkSolver(listOf(smallEvent(), smallEvent()), listOf(timeSlotsConstraints[3], timeSlotsConstraints[4]), false)
        checkSolver(listOf(longEvent()), listOf(timeSlotsConstraints[3], timeSlotsConstraints[4]), false)
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

        val timeSlotsConstraint = listOf(
            TimeSlotsConstraint(
                listOf(
                    TimeStamp(0).during(Duration(2)),
                    TimeStamp(5).during(Duration(4)),
                    TimeStamp(20).during(Duration(15))
                )
            )
        )
        checkSolver(smallEvents().take(3).toList(), timeSlotsConstraint, true)
        checkSolver(longEvents().take(1).toList(), timeSlotsConstraint, true)

        checkSolver(smallEvents().take(4).toList(), timeSlotsConstraint, false)
        checkSolver(longEvents().take(2).toList(), timeSlotsConstraint, false)
    }

    private fun checkSolver(
        events: List<Event>,
        constraints: List<RequiredConstraint>,
        solvable: Boolean
    ) {
        val eventsRequiredConstraint = EventsRequiredConstraint(events)
        val solution = MatchingSolver.solve(events, constraints + eventsRequiredConstraint)
        if (solvable) {
            for (constraint in constraints) {
                assertNotNull(solution)
                assertTrue { constraint.isSatisfied(solution) }
                assertTrue { eventsRequiredConstraint.isSatisfied(solution) }
            }
        } else {
            assertNull(solution)
        }
    }
}
