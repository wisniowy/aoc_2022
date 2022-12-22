package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay17Test {

    private val day = SolutionDay17()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(3068, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(1514285714288, day.partTwo())
    }
}