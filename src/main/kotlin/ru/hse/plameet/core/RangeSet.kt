package ru.hse.plameet.core

import java.lang.IllegalArgumentException

/**
 * Set that contains all integer between [lowerBound] (inclusive) and [upperBound] (exclusive)
 */
class RangeSet(private val lowerBound: Int, private val upperBound: Int) : AbstractSet<Int>() {
    override val size: Int
        get() = upperBound - lowerBound

    init {
        if (upperBound < lowerBound) {
            throw IllegalArgumentException("Upped bound should be more or equal than lower bound")
        }
    }

    override fun contains(element: Int): Boolean {
        return element in lowerBound until upperBound
    }

    override fun iterator(): Iterator<Int> {
        return object : Iterator<Int> {
            var currentInt = lowerBound

            override fun hasNext(): Boolean {
                return currentInt < upperBound
            }

            override fun next(): Int {
                return currentInt++
            }
        }
    }
}
