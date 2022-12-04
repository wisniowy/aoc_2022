package aoc.days

import aoc.utils.Reader

class SolutionDay3 : SolutionDay(3) {

    override fun partOne() : Any {
        return parseInput(Reader.inputAsStringList(day_n)).map m1@{
            val setOfPartTwo = it.second.toSet()
            return@m1 it.first.toList().map m2@{ item ->
                if (setOfPartTwo.contains(item)) {
                    return@m2 item.priority()
                }
                return@m2 0
            }.maxOrNull()
        }.filterNotNull().sum()
    }

    override fun partTwo() : Any {
        return Reader.inputAsStringList(day_n).chunked(3) { it ->
            return@chunked it.map { it.toSet() }
                .reduce {first, second -> first intersect second}
                .map { it.priority() }
                .maxOrNull() ?: 0
        }.sum()
    }

    private fun Char.priority(): Int {
        return if (this.isLowerCase()) this.code - 'a'.code + 1
                else this.code - 'A'.code + 26 + 1
    }

    private fun parseInput(input: List<String>): List<Pair<String, String>> {
       return input.map {
           val mid = it.length / 2
           return@map Pair(it.substring(0, mid), it.substring(mid))
       }
    }
}