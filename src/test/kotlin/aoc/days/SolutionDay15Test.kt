package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay15Test {

    private val day = SolutionDay15()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(26, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(56000011, day.partTwo())
    }
}