package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay8Test {

    private val day = SolutionDay8()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(21, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(8, day.partTwo())
    }
}