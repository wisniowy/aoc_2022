package aoc.days

import aoc.utils.Reader
import java.util.*
import kotlin.math.max
import kotlin.math.min


class SolutionDay20 : SolutionDay(20) {

    companion object {
        val DECRYPTION_KEY = 811589153L
    }

    private fun mixList(numbers: LinkedList<IndexedValue<Long>>) {
        numbers.indices.forEach{ index ->
            val currentIdx = numbers.indexOfFirst { it.index == index }
            val number = numbers.removeAt(currentIdx)
            numbers.add((currentIdx + number.value).mod(numbers.size), number)
        }
    }
    override fun partOne(): Any {
        val numbers = LinkedList(parseInput(Reader.inputAsStringList(day_n)).withIndex().toList())

        mixList(numbers)

        return listOf(1000, 2000, 3000)
            .sumOf { numbers[(numbers.indexOfFirst { it.value == 0L } + it).mod(numbers.size)].value }
    }

    override fun partTwo(): Any {
        val numbers = LinkedList(parseInput(Reader.inputAsStringList(day_n))
            .map { it * DECRYPTION_KEY }.withIndex().toList())

        repeat(10) {
            mixList(numbers)
        }

        return listOf(1000, 2000, 3000)
            .sumOf { numbers[(numbers.indexOfFirst { it.value == 0L } + it).mod(numbers.size)].value }
    }

    private fun parseInput(lines: List<String>): List<Long> {
        return lines.map { it.toLong() }.toList()
    }
}
