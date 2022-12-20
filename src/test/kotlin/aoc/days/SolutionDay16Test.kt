package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay16Test {

    private val day = SolutionDay16()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(1651, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(1707, day.partTwo())
    }
}