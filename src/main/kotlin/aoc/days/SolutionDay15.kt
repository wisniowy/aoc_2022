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

    private data class Node(val x: Int, val y: Int)

    private data class Beacon(val node: Node, val manhattanDistance: Int)

    companion object {
        val SENSOR_REGEX = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()
    }

    var PART_ONE_Y: Int? = null
    var PART_MAX_X_Y: Int? = null

    private fun occupiedXRange(sensorX: Int, sensorY: Int, y: Int, manhattanDistance: Int): Set<Int>? {
        val range = (sensorX - manhattanDistance + abs(sensorY - y))..(sensorX + manhattanDistance - abs(sensorY - y))
        return if (range.last < range.first) null else range.toSet()
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
        val possibleXRange: Set<Int> = (0..PART_MAX_X_Y!!).toSet().minus(manhattanDistances.map { (node, beacon) ->
            val minX = node.x - beacon.manhattanDistance
            val maxX = node.x + beacon.manhattanDistance
            return@map (max(minX, 0)..min(maxX, PART_MAX_X_Y!!)).toSet()
        }.reduce { s1, s2 -> s1.union(s2) })

        var beaconPosition: Node? = null

        Arrays.stream((0..PART_MAX_X_Y!!).toList().toIntArray()).parallel().forEach { y ->
            println(y)
            val occupiedXR = manhattanDistances.map m2@{ (node, beacon) -> return@m2 occupiedXRange(node.x, node.y, y, beacon.manhattanDistance) }
                .filterNotNull()
                .reduce { s1, s2 -> s1.union(s2) }

            val freeXR = possibleXRange.minus(occupiedXR)
            if (freeXR.isNotEmpty()) {
                beaconPosition = Node(freeXR.first(), y)
                return@forEach
            }
        }

        return 4000000 * beaconPosition!!.x + beaconPosition!!.y
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
