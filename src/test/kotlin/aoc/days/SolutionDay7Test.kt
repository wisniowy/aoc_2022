package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay7Test {

    private val day = SolutionDay7()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(95437L, day.partOne())
    }

    @Test
    fun partTwo() {
        kotlin.test.assertEquals(24933642L, day.partTwo())
    }
}