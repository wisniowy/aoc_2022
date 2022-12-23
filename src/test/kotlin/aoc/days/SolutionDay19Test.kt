package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay19Test {

    private val day = SolutionDay19()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(33, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(3472, day.partTwo())
    }
}