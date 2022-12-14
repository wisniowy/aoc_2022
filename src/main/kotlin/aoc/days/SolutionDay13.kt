package aoc.days

import aoc.utils.Reader

class SolutionDay13 : SolutionDay(13) {

    private fun compareNestedValues(left: NestedValue, right: NestedValue): Int {
        if (left.value != null && right.value != null) return compareNestedValues(left.value, right.value)
        else if (left.value == null && right.value == null) return compareNestedValues(left.nestedValues, right.nestedValues)
        else if (left.value != null) return compareNestedValues(NestedValue(null, listOf(left)), right)
        else if (right.value != null) return compareNestedValues(left, NestedValue(null, listOf(right)))
        return 3
    }

    private fun compareNestedValues(left: Int, right: Int): Int {
        return when {
            left < right -> 0
            left > right -> 1
            else -> 3
        }
    }

    private fun compareNestedValues(left: List<NestedValue>, right: List<NestedValue>): Int {
        var idx = 0

        while (idx < left.size && idx < right.size) {
            when (compareNestedValues(left[idx], right[idx])) {
                0 -> return 0
                1 -> return 1
            }

            idx += 1
        }

        return when {
            (idx == (left.size)) && (idx == (right.size)) -> 3
            idx == right.size -> 1
            idx == left.size -> 0
            else -> 3
        }
    }

    override fun partOne(): Any {
        val pairsOfPackets = parseInput(Reader.inputAsString(day_n))

        return pairsOfPackets.mapIndexed { idx, it -> Pair(idx + 1, compareNestedValues(it.first, it.second)) }
            .filter { it.second == 0 }
            .map { it.first }
            .sum()
    }


    data class NestedValueWrapper(val nestedValue: NestedValue, val divider: Boolean)

    override fun partTwo(): Any {
        val packets = parseInput(Reader.inputAsString(day_n)).map { it -> listOf(it.first, it.second) }
            .flatten()
            .map { it -> NestedValueWrapper(it, false) }
            .toMutableList()

        packets.add(NestedValueWrapper(parseList("[[2]]", 1).nestedValue, true))
        packets.add(NestedValueWrapper(parseList("[[6]]", 1).nestedValue, true))

        val myCustomComparator =  Comparator<NestedValueWrapper> { a, b ->
            val comparisonResult = compareNestedValues(a.nestedValue, b.nestedValue)
            when (comparisonResult) {
                0 -> -1
                1 -> 1
                else -> 0
            }
        }

        packets.sortWith(myCustomComparator)

        return packets.mapIndexed { idx, it ->
            return@mapIndexed when (it.divider == true) {
                true -> idx + 1
                false -> 1
            }
        }.reduce{ a: Int, b: Int -> a * b }
    }

    private fun parseInput(input: String): List<Pair<NestedValue, NestedValue>> {
        return input.split("\n\n").map {
            val splitted = it.split("\n")
            return@map Pair(parseList(splitted[0], 1).nestedValue, parseList(splitted[1], 1).nestedValue)
        }.toList()
    }

    data class NestedValue(val value: Int?, val nestedValues: List<NestedValue>)
    data class ParserOutput(val nestedValue: NestedValue, val lineIdx: Int)

    private fun parseList(line: String, lineIdx: Int): ParserOutput {
        var i = lineIdx
        val listOfNestedValues = mutableListOf<NestedValue>()
        val value: Int? = null

        while (i < line.length) {
            val c = line[i]
            when {
                c == '[' -> {
                    val output = parseList(line, i + 1)
                    i = output.lineIdx
                    listOfNestedValues.add(output.nestedValue)
                }
                c == ']' -> {
                    return ParserOutput(NestedValue(value, listOfNestedValues), i + 1)
                }
                c.isDigit() -> {
                    val output = parseNumber(line, i)
                    i = output.lineIdx
                    listOfNestedValues.add(output.nestedValue)
                }
                else -> i += 1
            }
        }
        return ParserOutput(NestedValue(value, listOfNestedValues), i + 1)
    }

    private fun parseNumber(line: String, lineIdx: Int): ParserOutput {
        var i = lineIdx
        val digits = mutableListOf<Char>()

        while (i < line.length) {
            val c = line[i]

            if(c.isDigit()) {
                digits.add(c)
            } else {
                return ParserOutput(NestedValue(digits.joinToString("").toInt(), listOf()), i)
            }
            i += 1
        }

        error("illegal state")
    }
}
