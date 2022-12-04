package aoc.days

import aoc.utils.Reader

class SolutionDay4 : SolutionDay(4) {

    override fun partOne() : Any {
        return parseInput(Reader.inputAsStringList(day_n)).map {
            val firstRangeSplit = it.first.split('-')
            val secondRangeSplit = it.second.split('-')

            val firstRange = IntRange(firstRangeSplit[0].toInt(), firstRangeSplit[1].toInt())
            val secondRange = IntRange(secondRangeSplit[0].toInt(), secondRangeSplit[1].toInt())

            return@map if ((secondRange.start >= firstRange.start && secondRange.endInclusive <= firstRange.endInclusive)
                || (firstRange.start >= secondRange.start && firstRange.endInclusive <= secondRange.endInclusive)) 1
            else 0
        }.sum()
    }

    override fun partTwo() : Any {
        return parseInput(Reader.inputAsStringList(day_n)).map {
            val firstRangeSplit = it.first.split('-')
            val secondRangeSplit = it.second.split('-')

            val firstRange = IntRange(firstRangeSplit[0].toInt(), firstRangeSplit[1].toInt())
            val secondRange = IntRange(secondRangeSplit[0].toInt(), secondRangeSplit[1].toInt())

            return@map if (firstRange.intersect(secondRange).size > 0) 1
                else 0
        }.sum()
    }

    private fun parseInput(input: List<String>): List<Pair<String, String>> {
       return input.map {
           val split = it.split(',')
           return@map Pair(split[0], split[1])
       }
    }
}