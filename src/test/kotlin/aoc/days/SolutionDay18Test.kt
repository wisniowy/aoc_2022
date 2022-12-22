package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay18Test {

    private val day = SolutionDay18()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(64, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(58, day.partTwo())
    }
}