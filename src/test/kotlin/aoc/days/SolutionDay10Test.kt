package aoc.days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SolutionDay10Test {

    private val day = SolutionDay10()

    @Test
    fun partOne() {
        kotlin.test.assertEquals(13140, day.partOne())
    }

    @Test
    fun partTwo() {
        val expectedOutput =
                "##..##..##..##..##..##..##..##..##..##..\n" +
                "###...###...###...###...###...###...###.\n" +
                "####....####....####....####....####....\n" +
                "#####.....#####.....#####.....#####.....\n" +
                "######......######......######......####\n" +
                "#######.......#######.......#######....."
        kotlin.test.assertEquals(expectedOutput, day.partTwo())
    }
}