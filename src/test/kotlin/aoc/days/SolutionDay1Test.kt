package aoc.days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class SolutionDay1Test {

    private val dayOne = SolutionDay1()

    @Test
    fun partOne() {
        assertEquals(24000, dayOne.partOne())
    }

    @Test
    fun partTwo() {
        assertEquals(45000, dayOne.partTwo())
    }
}