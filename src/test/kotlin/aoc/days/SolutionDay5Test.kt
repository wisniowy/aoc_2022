package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay5Test {

    private val day = SolutionDay5()

    @Test
    fun partOne() {
        kotlin.test.assertEquals("CMZ", day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals("MCD", day.partTwo())
    }
}