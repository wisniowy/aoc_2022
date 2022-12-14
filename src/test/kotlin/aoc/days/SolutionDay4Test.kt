package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay4Test {

    private val day = SolutionDay4()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(2, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(4, day.partTwo())
    }
}