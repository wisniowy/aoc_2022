package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay11Test {

    private val day = SolutionDay11()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(10605L, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(2713310158L, day.partTwo())
    }
}