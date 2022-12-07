package aoc.days

import aoc.utils.Reader

class SolutionDay6 : SolutionDay(6) {

    override fun partOne() : Any {
        val window = 4
        val datastream = Reader.inputAsString(day_n)

        datastream.withIndex().windowed(window).forEach { charsWindow ->
            if (charsWindow.map { it.value }.distinct().size == window) {
                return charsWindow[window - 1].index + 1
            }

        }

        return 0
    }

    override fun partTwo() : Any {
        val window = 14
        val datastream = Reader.inputAsString(day_n)

        datastream.withIndex().windowed(window).forEach { charsWindow ->
            if (charsWindow.map { it.value }.distinct().size == window) {
                return charsWindow[window - 1].index + 1
            }

        }

        return 0
    }
}
