package aoc.days

import aoc.utils.Reader
import kotlin.math.max
import kotlin.math.pow


class SolutionDay17 : SolutionDay(17) {

    companion object {
        val SHAPES_STR = """
            ####

            .#.
            ###
            .#.

            ..#
            ..#
            ###

            #
            #
            #
            #

            ##
            ##
        """.trimIndent()

        val SHAPES = parseShapes(SHAPES_STR.split("\n\n"))

        private fun parseShapes(input: List<String>): List<Shape> {
            return input.map { shapeStr ->
                val nodes = mutableSetOf<Node>()
                shapeStr.split("\n").reversed().forEachIndexed { y, line ->
                    line.forEachIndexed { x, c ->
                        c.takeIf { it == '#' }?.apply { nodes.add(Node(x, y)) }
                    }
                }
                return@map Shape(nodes)
            }
        }
    }

    enum class Direction(val xOffset: Int, val yOffset: Int) {
        LEFT(-1, 0), RIGHT(1, 0), DOWN(0, -1)
    }

    data class Node(var x: Int, var y: Int)
    data class Shape(val nodes: Set<Node>)
    data class RowState(val shapeIdx: Int, val directionIdx: Int, val rowValue: Int)
    data class MapState(val numOfUnits: Int, val shapeIdx: Int)

    fun Node.move(xOffset: Int, yOffset: Int) {
        this.x = this.x + xOffset
        this.y = this.y + yOffset
    }

    fun Shape.move(xOffset: Int, yOffset: Int) {
        nodes.forEach { it.move(xOffset, yOffset) }
    }

    fun Shape.deepCopy(): Shape {
        return Shape(this.nodes.map { it.copy() }.toSet())
    }

    fun Shape.maY(): Int {
        return nodes.map { it.y }.maxOrNull() ?: 0
    }

    fun <T> List<T>.getElementInCycle(i: Int): T {
        return this[i % this.size]
    }


    override fun partOne(): Any {
        val directions = parseInput(Reader.inputAsString(day_n))
        val map = mutableSetOf<Node>().apply { addAll((1..8).map { Node(it, 0) }) }

        val heights = mutableListOf<Int>()
        var shapeIdx = 0
        var directionIdx = 0
        var newShape: Shape? = null
        var currentDirection: Direction? = null

        while (shapeIdx <= 2022) {
            currentDirection = directions.getElementInCycle(directionIdx++)
            newShape = newShape ?: SHAPES.getElementInCycle(shapeIdx++).deepCopy().apply { move(3, (heights.lastOrNull()?: 0) + 4) }

            ((heights.lastOrNull()?: 0)..newShape.maY()).forEach {
                map.add(Node(0, it))
                map.add(Node(8, it))
            }

            if (newShape.nodes.none { node ->
                    map.contains(Node(node.x + currentDirection.xOffset, node.y + currentDirection.yOffset)) }) {
                newShape.move(currentDirection.xOffset, currentDirection.yOffset)
            }

            if (newShape.nodes.none { node ->
                    map.contains(Node(node.x + Direction.DOWN.xOffset, node.y + Direction.DOWN.yOffset)) }) {
                newShape.move(Direction.DOWN.xOffset, Direction.DOWN.yOffset)
                continue
            }

            map.addAll(newShape.nodes)
            heights.add(max(heights.lastOrNull() ?: 0, newShape.maY()))
            newShape = null
        }

        return heights.last()
    }

    override fun partTwo(): Any {
        val directions = parseInput(Reader.inputAsString(day_n))
        val map = mutableSetOf<Node>().apply { addAll((1..8).map { Node(it, 0) }) }

        val rowsStates = mutableMapOf<RowState, MutableList<MapState>>()
        val heights = mutableListOf<Int>()
        var shapeIdx = 0
        var directionIdx = 0
        var newShape: Shape? = null
        var currentDirection: Direction? = null

        while (shapeIdx < 10000000) {
            currentDirection = directions.getElementInCycle(directionIdx++)
            newShape = newShape ?: SHAPES.getElementInCycle(shapeIdx++).deepCopy().apply { move(3, (heights.lastOrNull()?: 0) + 4) }

            ((heights.lastOrNull()?: 0)..newShape.maY()).forEach {
                map.add(Node(0, it))
                map.add(Node(8, it))
            }

            if (newShape.nodes.none { node ->
                    map.contains(Node(node.x + currentDirection.xOffset, node.y + currentDirection.yOffset)) }) {
                newShape.move(currentDirection.xOffset, currentDirection.yOffset)
            }

            if (newShape.nodes.none { node ->
                    map.contains(Node(node.x + Direction.DOWN.xOffset, node.y + Direction.DOWN.yOffset)) }) {
                newShape.move(Direction.DOWN.xOffset, Direction.DOWN.yOffset)
                continue
            }

            map.addAll(newShape.nodes)
            heights.add(max(heights.lastOrNull() ?: 0, newShape.maY()))
            newShape = null

            val rowMaxState = let {
                val rowMaxValue = (1..8).sumOf {
                    if (map.contains(Node(it, heights.last()))) (10).toDouble().pow(it).toInt() else 0
                }
                return@let RowState(shapeIdx % SHAPES.size, directionIdx % directions.size, rowMaxValue)
            }

            val prevMapStates = rowsStates.computeIfAbsent(rowMaxState) { mutableListOf() }.apply { add(MapState(heights.last(), shapeIdx)) }

            if (prevMapStates.size > 1000) {
                val firstCycleStartState = let {
                    val shifts = prevMapStates.windowed(2).map { it[1].shapeIdx - it[0].shapeIdx }
                    val mostOccurrencesCycleSize = shifts.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key
                    return@let prevMapStates[shifts.indexOfFirst { it == mostOccurrencesCycleSize }]
                }

                val remainingShapes = 1000000000000L - firstCycleStartState.shapeIdx
                val numOfShapesInCycle = shapeIdx - firstCycleStartState.shapeIdx
                val numOfCycles = remainingShapes / numOfShapesInCycle
                val shapesOutOfCycle = remainingShapes % numOfShapesInCycle
                val unitsInCycle = heights.last() - heights[firstCycleStartState.shapeIdx - 1]
                return numOfCycles * unitsInCycle + (heights[firstCycleStartState.shapeIdx + shapesOutOfCycle.toInt()]
                        - heights[firstCycleStartState.shapeIdx]) + heights[firstCycleStartState.shapeIdx] - 1
            }
        }

        return 0
    }

    private fun parseInput(input: String): MutableList<Direction> {
        return input.map { c ->
            when (c) {
                '<' -> Direction.LEFT
                '>' -> Direction.RIGHT
                else -> error("illegal state")
            }
        }.toMutableList()
    }
}
