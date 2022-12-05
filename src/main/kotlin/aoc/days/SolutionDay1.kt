package aoc.days

import aoc.utils.Reader

class SolutionDay1 : SolutionDay(1) {

    override fun partOne() : Any {
        return parseInput(Reader.inputAsString(day_n))
            .map { it.sum() }
            .sortedDescending()
            .take(1)
            .sum()
    }

    override fun partTwo() : Any {
        return parseInput(Reader.inputAsString(day_n))
            .map { it.sum() }
            .sortedDescending()
            .take(3)
            .sum()
    }

    private fun parseInput(input: String): List<List<Int>> {
       return input.split("\n\n")
            .map { it.lines().map { it.toInt() } }
            .toList()
    }
}