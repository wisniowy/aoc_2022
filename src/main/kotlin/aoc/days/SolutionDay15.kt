package aoc.days

import aoc.utils.Reader
import java.lang.Integer.max
import java.util.*
import kotlin.math.abs
import kotlin.math.min

/*
    x1 = Sx
    y1 = Sy
    y2 = C
    D - distance of beacon

    |x1 - x2| + |y1 - y2| <= D
    |Sx - x2| + |Sy - C| <= D
    |Sx - x2| <= D - |Sy - C|

    Sx - x2 <= D - |Sy - C|  and  Sx - x2 >= -D + |Sy - C|
    -x2 <= D - |Sy - C| - SX and  - x2 >= -D + |Sy - C| - Sx
    x2 >= -D + |Sy - C| + SX and  x2 <= D - |Sy - C| + Sx
 */
class SolutionDay15 : SolutionDay(15) {

    private enum class EntityType {
        SENSOR, BEACON, BEACON_RANGE, EMPTY
    }

    private data class Entity(val entityType: EntityType, val sensorId: Int)

    data class Node(val x: Int, val y: Int)

    private data class Beacon(val node: Node, val manhattanDistance: Int)

    fun Node.getMDistanceEdgeNodes(manhattanDistance: Int): Sequence<Node> {
        val sensorX = this.x
        val sensorY = this.y
        val topNode = Node(sensorX, sensorY - manhattanDistance)
        val bottomNode = Node(sensorX, sensorY + manhattanDistance)

        return sequence {
            yield(topNode)
            var leftNode = Node(topNode.x - 1, topNode.y + 1)
            var rightNode = Node(topNode.x + 1, topNode.y + 1)
            while (leftNode.x >= sensorX - manhattanDistance && leftNode.y <= sensorY) {
                yield(leftNode)
                leftNode = Node(leftNode.x - 1, leftNode.y + 1)
                yield(rightNode)
                rightNode = Node(rightNode.x + 1, rightNode.y + 1)
            }

            yield(bottomNode)
            leftNode = Node(bottomNode.x - 1, bottomNode.y - 1)
            rightNode = Node(bottomNode.x + 1, bottomNode.y - 1)
            while (leftNode.x >= sensorX - manhattanDistance && leftNode.y >= sensorY) {
                yield(leftNode)
                leftNode = Node(leftNode.x - 1, leftNode.y - 1)
                yield(rightNode)
                rightNode = Node(rightNode.x + 1, rightNode.y - 1)
            }
        }
    }

    companion object {
        val SENSOR_REGEX = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()
        val DIRECTIONS = listOf(Pair(-1, -1),
            Pair(0, -1),
            Pair(1, -1),
            Pair(-1, 0),
            Pair(1, 0),
            Pair(-1, 1),
            Pair(0, 1),
            Pair(1, 1)
        )
    }

    var PART_ONE_Y: Int? = null
    var PART_MAX_X_Y: Int? = null

    private fun occupiedXRange(sensorX: Int, sensorY: Int, y: Int, manhattanDistance: Int): Set<Int>? {
        val range = (sensorX - manhattanDistance + abs(sensorY - y))..(sensorX + manhattanDistance - abs(sensorY - y))
        return if (range.last < range.first) null else range.toSet()
    }

    private fun isOccupied(sensorX: Int, sensorY: Int, x: Int, y: Int, manhattanDistance: Int): Boolean {
        if (sensorX == x && sensorY == y) return false
        val range = (sensorX - manhattanDistance + abs(sensorY - y))..(sensorX + manhattanDistance - abs(sensorY - y))
        return if (range.last < range.first) false else range.contains(x)
    }

    override fun partOne(): Any {
        val manhattanDistances = parseInput(Reader.inputAsStringList(day_n))
        val beaconsAtGivenY = manhattanDistances.values.map { it.node }.filter { it.y == PART_ONE_Y }.distinct()

        val occupiedXR = manhattanDistances.map { (node, beacon) -> return@map occupiedXRange(node.x, node.y, PART_ONE_Y!!, beacon.manhattanDistance) }
            .filterNotNull()
            .reduce { s1, s2 -> s1.union(s2) }
            .filter { !beaconsAtGivenY.map { it.x }.contains(it) }

        return occupiedXR.size
    }

    override fun partTwo(): Any {
        val manhattanDistances = parseInput(Reader.inputAsStringList(day_n))

        var beaconNode: Node? = null

        manhattanDistances.forEach m1@{ (sensor, beacon) ->
            sensor.getMDistanceEdgeNodes(beacon.manhattanDistance + 1).forEach  m2@{ edgeNode ->
                if (edgeNode.x >= 0 && edgeNode.x <= PART_MAX_X_Y!! && edgeNode.y >= 0 && edgeNode.y <= PART_MAX_X_Y!!) {
                val isNodeOccupied = manhattanDistances.map { (s, b) ->
                    isOccupied(s.x, s.y, edgeNode.x, edgeNode.y, b.manhattanDistance)
                }.any { it == true }

                if (isNodeOccupied == false) {
                    beaconNode = edgeNode
                    return@m1
                } }}
        }

        return 4000000L * beaconNode!!.x.toLong() + beaconNode!!.y.toLong()
    }

    private fun parseInput(lines: List<String>): Map<Node, Beacon> {
        val map = mutableMapOf<Node, Entity>()
        val manhattanDistances = mutableMapOf<Node, Beacon>()

        PART_ONE_Y = lines[0].toInt()
        PART_MAX_X_Y = lines[1].toInt()

        lines.subList(2, lines.size).forEachIndexed{ idx, line ->
            val sensorMatch = SENSOR_REGEX.find(line)
            val (sensorX, sensorY, beaconX, beaconY) = sensorMatch!!.destructured
            val sensorNode = Node(sensorX.toInt(), sensorY.toInt())
            val beaconNode = Node(beaconX.toInt(), beaconY.toInt())
            map[sensorNode] = Entity(EntityType.SENSOR, idx)
            map[beaconNode] = Entity(EntityType.BEACON, idx)
            manhattanDistances[sensorNode] = Beacon(beaconNode,
                abs(sensorX.toInt() - beaconX.toInt()) + abs(sensorY.toInt() - beaconY.toInt()))
        }

        return manhattanDistances
    }
}
