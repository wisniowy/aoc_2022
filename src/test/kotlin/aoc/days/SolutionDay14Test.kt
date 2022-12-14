package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay14Test {

    private val day = SolutionDay14()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(24, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(93, day.partTwo())
    }
}