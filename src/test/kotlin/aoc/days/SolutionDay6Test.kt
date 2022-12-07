package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay6Test {

    private val day = SolutionDay6()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(10, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(29, day.partTwo())
    }
}