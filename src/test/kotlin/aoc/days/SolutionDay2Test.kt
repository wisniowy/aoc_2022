package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay2Test {

    private val day = SolutionDay2()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(15, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(12, day.partTwo())
    }
}