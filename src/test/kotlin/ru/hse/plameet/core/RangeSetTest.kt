package ru.hse.plameet.core

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RangeSetTest {

    @Test
    fun sizeTest() {
        var rangeSet = RangeSet(10, 20)
        assertEquals(10, rangeSet.size)
        assertFalse(rangeSet.isEmpty())

        rangeSet = RangeSet(0, 0)
        assertEquals(0, rangeSet.size)
        assertTrue(rangeSet.isEmpty())

        rangeSet = RangeSet(-3, 4)
        assertEquals(7, rangeSet.size)
    }

    @Test
    fun testContains() {
        var rangeSet = RangeSet(4, 7)
        assertTrue(rangeSet.containsAll((4..6).toList()))
        assertFalse(rangeSet.contains(7))
        assertFalse(rangeSet.contains(3))

        rangeSet = RangeSet(-1, 3)
        assertTrue(rangeSet.contains(-1))
        assertTrue(rangeSet.contains(0))
        assertTrue(rangeSet.contains(1))
        assertTrue(rangeSet.contains(2))
        assertFalse(rangeSet.contains(3))
        assertFalse(rangeSet.contains(12))
        assertFalse(rangeSet.contains(-2))
    }

    @Test
    fun testIterator() {
        var rangeSet = RangeSet(3, 10)
        assertTrue(rangeSet.all { it in 3..9 })
        for (i in rangeSet) {
            assertTrue { i in 3..9 }
        }

        rangeSet = RangeSet(0, 0)
        for (i in rangeSet) {
            assertTrue(false)
        }
    }

    @Test
    fun testInitExceptions() {
        assertThrows<IllegalArgumentException> { RangeSet(3, 2) }
        assertThrows<IllegalArgumentException> { RangeSet(-1, -3) }
        assertThrows<IllegalArgumentException> { RangeSet(1, -3) }
        assertDoesNotThrow { RangeSet(1, 3) }
    }
}
