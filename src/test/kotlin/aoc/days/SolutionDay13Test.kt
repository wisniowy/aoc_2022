package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay13Test {

    private val day = SolutionDay13()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(13, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(140, day.partTwo())
    }
}