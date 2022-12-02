package aoc.days

import aoc.utils.Reader

class SolutionDay1 {

    fun partOne() : Any {
        return parseInput(Reader.inputAsString(1))
            .map { it.sum() }
            .sortedDescending()
            .take(1)
            .sum()
    }

    fun partTwo() : Any {
        return parseInput(Reader.inputAsString(1))
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