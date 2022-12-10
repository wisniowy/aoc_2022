package aoc.days

import aoc.utils.Reader
import kotlin.math.abs

class SolutionDay9 : SolutionDay(9) {

    private data class Position(var x: Int, var y: Int)
    private data class Direction(val x: Int, val y: Int)
    private data class Command(val direction: Direction, val step: Int)

    private fun Char.toDirection(): Direction {
        return when (this) {
            'L' -> Direction(-1, 0)
            'R' -> Direction(1, 0)
            'U' -> Direction(0, 1)
            'D' -> Direction(0, -1)
            else -> error("wrong input")
        }
    }

    override fun partOne(): Any {
        val commands = parseInput(Reader.inputAsStringList(day_n))
        val headPosition = Position(0, 0)
        val tailPositions = ArrayDeque<Position>()
        tailPositions.addLast(Position(0, 0))

        commands.forEach { command ->
            repeat(command.step) {
                headPosition.x += command.direction.x
                headPosition.y += command.direction.y

                val tailPosition = tailPositions.last()

                if (abs(headPosition.x - tailPosition.x) > 1
                    || abs(headPosition.y - tailPosition.y) > 1) {
                    val newTailPosition = Position(tailPosition.x, tailPosition.y)
                    newTailPosition.x += (headPosition.x - tailPosition.x) / Math.max(
                        abs(headPosition.x - tailPosition.x), 1)
                    newTailPosition.y += (headPosition.y - tailPosition.y) / Math.max(abs(
                        headPosition.y - tailPosition.y), 1)

                    tailPositions.addLast(newTailPosition)
                }
            }
        }

        return tailPositions.toSet().size
    }

    override fun partTwo(): Any {
        val commands = parseInput(Reader.inputAsStringList(day_n))
        val headPosition = Position(0, 0)
        val knotsPositions = Array<Position>(9) { i -> Position(0, 0) }

        val tailPositions = mutableSetOf<Position>()
        tailPositions.add(Position(0, 0))

        commands.forEach { command ->
            repeat(command.step) {
                headPosition.x += command.direction.x
                headPosition.y += command.direction.y

                for (i in knotsPositions.indices) {
                    val knotPosition = knotsPositions[i]
                    val prevKnotPosition = if (i != 0) knotsPositions[i - 1] else headPosition

                    if (abs(prevKnotPosition.x - knotPosition.x) > 1
                        || abs(prevKnotPosition.y - knotPosition.y) > 1) {
                        val newKnotPosition = Position(knotPosition.x, knotPosition.y)
                        newKnotPosition.x += (prevKnotPosition.x - knotPosition.x) / Math.max(
                            abs(prevKnotPosition.x - knotPosition.x), 1)
                        newKnotPosition.y += (prevKnotPosition.y - knotPosition.y) / Math.max(
                            abs(prevKnotPosition.y - knotPosition.y), 1)

                       knotsPositions[i] = newKnotPosition
                    }
                }
                tailPositions.add(knotsPositions.last())
            }
        }

        return tailPositions.size
    }

    private fun parseInput(lines: List<String>): List<Command> {
        return lines.map {
            val line = it.split(" ")
            val direction = line[0][0].toDirection()
            val step = line[1].toInt()
            return@map Command(direction, step)
        }.toList()
    }
}
