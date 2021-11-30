package ru.hse.plameet.core

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

    override fun iterator(): IntIterator {
        return (lowerBound until upperBound).iterator()
    }
}
