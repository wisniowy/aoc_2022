package aoc.days

import aoc.utils.Reader

class SolutionDay12 : SolutionDay(12) {

    companion object {
        val DIRECTIONS = listOf(Pair(0, 1), Pair(0, -1), Pair(-1, 0), Pair(1, 0))
    }

    data class Node(val x: Int, val y: Int)

    data class NodeMap(val start: Node, val end: Node, val nodes: Map<Node, Int>)

    fun Char.toElevation(): Int {
        return when (this) {
            'S' -> 'a'.code - 'a'.code
            'E' -> 'z'.code - 'a'.code
            else -> this.code - 'a'.code
        }
    }

    override fun partOne(): Any {
        val nodeMap = parseInput(Reader.inputAsStringList(day_n))
        val nodes = nodeMap.nodes
        val startNode = nodeMap.start
        val endNode = nodeMap.end

        return getShortestTrail(nodes, startNode, endNode)
    }

    private fun getShortestTrail(nodes: Map<Node, Int>, startNode: Node, endNode: Node): Int {
        val visitedNodes = nodes.keys.map { it to false }.toMap().toMutableMap()
        val distance = nodes.keys.map { it to Int.MAX_VALUE }.toMap().toMutableMap()
        distance[startNode] = 0

        nodes.forEach {
            val minDistNode = minDistance(distance, visitedNodes)
            visitedNodes[minDistNode] = true

            getNeighbours(nodes, minDistNode).forEach { n ->
                if (!visitedNodes[n]!!
                    && distance[minDistNode]!! != Int.MAX_VALUE
                    && distance[minDistNode]!! + 1 < distance[n]!!
                ) {
                    distance[n] = distance[minDistNode]!! + 1
                }
            }
        }
        return distance[endNode]!!
    }

    private fun minDistance(dist: Map<Node, Int>, visited: Map<Node, Boolean>): Node {
        var minDist = Int.MAX_VALUE
        var minDistNode: Node? = null

        dist.forEach { node, distance ->
            if (!visited[node]!! && distance <= minDist) {
                minDist = distance
                minDistNode = node
            }
        }
        return minDistNode!!
    }

    private fun getNeighbours(nodes: Map<Node, Int>, node: Node): List<Node> {
        return DIRECTIONS.map { direction -> Node(node.x - direction.first, node.y - direction.second) }
            .filter { nodes.contains(it) && (nodes[it]!! - nodes[node]!!) <= 1 }
            .toList()
    }

    override fun partTwo(): Any {
        val nodeMap = parseInput(Reader.inputAsStringList(day_n))
        val nodes = nodeMap.nodes
        val endNode = nodeMap.end

        val minElevation = nodes.values.minOrNull()!!

        return nodes.filter { it.value == minElevation }
            .map { getShortestTrail(nodes, it.key, endNode) }
            .minOrNull()!!
    }

    private fun parseInput(lines: List<String>): NodeMap {
        val nodes = mutableMapOf<Node, Int>()
        var startNode: Node? = null
        var endNode: Node? = null

        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val node = Node(x, y)

                when (c) {
                    'S' -> startNode = node
                    'E' -> endNode = node
                }

                val elevation = c.toElevation()

                nodes.put(node, elevation)
            }
        }

        return NodeMap(startNode!!, endNode!!, nodes)
    }
}
