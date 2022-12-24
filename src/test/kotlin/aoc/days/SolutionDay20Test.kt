package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay20Test {

    private val day = SolutionDay20()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(3L, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(1623178306L, day.partTwo())
    }
}