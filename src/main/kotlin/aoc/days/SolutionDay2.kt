package aoc.days

import aoc.utils.Reader

class SolutionDay2 : SolutionDay(2) {

    override fun partOne() : Any {
        return parseInput(Reader.inputAsStringList(day_n)).map {
            val opponent = it.first.toShapeScore()
            val player = it.second.toShapeScore()

            if (opponent == player) {
                return@map player + GameResult.TIE.score
            } else if ((opponent + 3) % 3 + 1 == player) {
                return@map player + GameResult.WIN.score
            } else {
                return@map player + GameResult.LOSS.score
            }
        }.sum()
    }

//    (startingValue - minimumValue + (offset % modulus) + modulus) % modulus + minimalValue

    override fun partTwo() : Any {
        return parseInput(Reader.inputAsStringList(day_n)).map {
            val opponentScore = it.first.toShapeScore()
            val gameScore = it.second.toGameScore()

            return@map when (gameScore) {
                GameResult.LOSS -> (opponentScore - 1 + (-1) % 3 + 3) % 3 + 1
                GameResult.TIE -> opponentScore
                GameResult.WIN -> (opponentScore + 3) % 3 + 1
            } + gameScore.score
        }.sum()
    }

    private enum class GameResult(val score: Int) {
        LOSS(0),
        TIE(3),
        WIN(6)
    }

    private fun Char.toShapeScore(): Int {
        return when(this) {
            'A', 'X' -> 1
            'B', 'Y' -> 2
            'C', 'Z' -> 3
            else -> error("wrong argument")
        }
    }

    private fun Char.toGameScore(): GameResult {
        return when(this) {
            'X' -> GameResult.LOSS
            'Y' -> GameResult.TIE
            'Z' -> GameResult.WIN
            else -> error("wrong argument")
        }
    }

    private fun parseInput(input: List<String>): List<Pair<Char, Char>> {
       return input.map { Pair(it[0], it[2]) }
    }
}