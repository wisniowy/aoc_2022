package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay9Test {

    private val day = SolutionDay9()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(13, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(36, day.partTwo())
    }
}