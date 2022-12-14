package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay12Test {

    private val day = SolutionDay12()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(31, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(29, day.partTwo())
    }
}