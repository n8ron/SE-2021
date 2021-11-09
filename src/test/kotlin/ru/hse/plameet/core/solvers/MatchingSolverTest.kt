package ru.hse.plameet.core.solvers

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.hse.plameet.core.*
import ru.hse.plameet.core.constraints.BooleanConstraint
import ru.hse.plameet.core.constraints.EventsRequiredConstraint
import ru.hse.plameet.core.constraints.TimeSlotsConstraint
import ru.hse.plameet.core.constraints.UserAvailabilityConstraint
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

        val twoSlotsConstraints = listOf(
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
            )
        )

        assertThrows<IllegalArgumentException> { checkSolver(listOf(smallEvent()), twoSlotsConstraints, true) }
        assertThrows<IllegalArgumentException> { checkSolver(listOf(smallEvent(), longEvent()), listOf(), false) }
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

    @Test
    fun testAvailabilityConstraint() {
        val users = listOf(User(0), User(1), User(2), User(3))
        val events = listOf(
            Event(0, Duration(1), listOf(users[0], users[1])),
            Event(1, Duration(2), listOf(users[2], users[3])),
            Event(2, Duration(2), listOf(users[0], users[2]))
        )

        val slots = TimeSlotsConstraint(listOf(TimeStamp(1), TimeStamp(5), TimeStamp(9)), Duration(2))

        val availability1 = UserAvailabilityConstraint(
            mapOf(
                users[0] to listOf(slots.sortedSlots[0], slots.sortedSlots[1]),
                users[1] to listOf(slots.sortedSlots[1]),
                users[2] to listOf(slots.sortedSlots[0], slots.sortedSlots[2]),
                users[3] to listOf(slots.sortedSlots[2])
            )
        )
        checkSolver(events, listOf(slots, availability1), true)

        val availability2 = UserAvailabilityConstraint(
            mapOf(
                users[0] to listOf(slots.sortedSlots[0], slots.sortedSlots[1]),
                users[1] to listOf(slots.sortedSlots[1]),
                users[2] to listOf(slots.sortedSlots[0], slots.sortedSlots[1]),
                users[3] to listOf(slots.sortedSlots[2])
            )
        )
        checkSolver(events, listOf(slots, availability2), false)

        val availability3 = UserAvailabilityConstraint(
            mapOf(
                users[0] to listOf(slots.sortedSlots[0], slots.sortedSlots[1]),
                users[1] to listOf(slots.sortedSlots[0]),
                users[2] to listOf(slots.sortedSlots[0], slots.sortedSlots[2]),
                users[3] to listOf(slots.sortedSlots[2])
            )
        )
        checkSolver(events, listOf(slots, availability3), false)
    }

    private fun checkSolver(
        events: List<Event>,
        constraints: List<BooleanConstraint>,
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
