package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay3Test {

    private val day = SolutionDay3()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(157, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(70, day.partTwo())
    }
}