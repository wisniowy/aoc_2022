package aoc.days

import aoc.utils.Reader

class SolutionDay14 : SolutionDay(14) {

    private enum class Tile(val c: Char) {
        ROCK('#'), AIR('.'), SAND('o'), SOURCE('+')
    }

    data class Node(val x: Int, val y: Int)
    data class NodeWrapper(val node: Node, val outOfMap: Boolean)

    val sourceNode = Node(500, 0)

    private fun placeTile(tiles: MutableMap<Node, Tile>, sandUnitNode: Node, infinite: Boolean = false): NodeWrapper? {
        var nodePlacement: NodeWrapper? = null

        when {
            !tiles.contains(sandUnitNode) && !infinite -> return NodeWrapper(sandUnitNode, true)
            !tiles.contains(sandUnitNode) && infinite -> {
                val maxY = tiles.keys.map { it.y }.maxOrNull()!!

                (0..maxY).forEach { y ->
                    val tile = when (y) {
                        maxY -> Tile.ROCK
                        else -> Tile.AIR
                    }
                    tiles[Node(sandUnitNode.x, y)] = tile
                }
                return placeTile(tiles, sandUnitNode, infinite)
            }
        }

        if (tiles[sandUnitNode] != Tile.AIR) {
            return null
        }

        nodePlacement = placeTile(tiles, Node(sandUnitNode.x, sandUnitNode.y + 1), infinite)
        if (nodePlacement != null) return nodePlacement

        nodePlacement = placeTile(tiles, Node(sandUnitNode.x - 1, sandUnitNode.y + 1), infinite)
        if (nodePlacement != null) return nodePlacement

        nodePlacement = placeTile(tiles, Node(sandUnitNode.x + 1, sandUnitNode.y + 1), infinite)
        if (nodePlacement != null) return nodePlacement

        return NodeWrapper(sandUnitNode, false)
    }

    override fun partOne(): Any {
        val tiles = parseInput(Reader.inputAsStringList(day_n))

        var sandUnitNode = Node(sourceNode.x, sourceNode.y + 1)

        var count = 0
        while (true) {
            val nodeCandidate = Node(sandUnitNode.x, sandUnitNode.y + 1)
            when {
                tiles[nodeCandidate] != null && tiles[nodeCandidate] == Tile.AIR -> {
                    sandUnitNode = Node(sandUnitNode.x, sandUnitNode.y + 1)
                }

                tiles[nodeCandidate] != null && tiles[nodeCandidate] != Tile.AIR -> {
                    val nodePlacement = placeTile(tiles, sandUnitNode)!!

                    if (nodePlacement.outOfMap) {
                        break
                    }

                    tiles[nodePlacement.node] = Tile.SAND
                    sandUnitNode = Node(sourceNode.x, sourceNode.y - 1)
                    count += 1
                }

            }
        }

        return count
    }

    override fun partTwo(): Any {
        val tiles = parseInput(Reader.inputAsStringList(day_n))

        var sandUnitNode = Node(sourceNode.x, sourceNode.y)
        val minX = tiles.keys.map { it.x }.minOrNull()!!
        val maxX = tiles.keys.map { it.x }.maxOrNull()!!
        val maxY = tiles.keys.map { it.y }.maxOrNull()!!

        (maxY + 1..(maxY + 2)).forEach { y ->
            (minX..maxX).forEach { x ->
                val tile = when (y) {
                    maxY + 2 -> Tile.ROCK
                    else -> Tile.AIR
                }
                tiles[Node(x, y)] = tile
            }
        }

        var count = 0
        while (true) {
            val nodeCandidate = Node(sandUnitNode.x, sandUnitNode.y + 1)
            when {
                tiles[nodeCandidate] != null && tiles[nodeCandidate] == Tile.AIR -> {
                    sandUnitNode = Node(sandUnitNode.x, sandUnitNode.y + 1)
                }

                tiles[nodeCandidate] != null && tiles[nodeCandidate] != Tile.AIR -> {
                    val nodePlacement = placeTile(tiles, sandUnitNode, true)

                    if (nodePlacement == null) {
                        break
                    }

                    tiles[nodePlacement.node] = Tile.SAND
                    sandUnitNode = Node(sourceNode.x, sourceNode.y)
                    count += 1
                }

            }
        }

        return count
    }

    private fun parseInput(lines: List<String>): MutableMap<Node, Tile> {
        val tiles: MutableMap<Node, Tile> = mutableMapOf()

        val rockPaths = lines.map m1@{ line ->
            val paths = line.split(" -> ")

            return@m1 paths.map m2@{ path ->
                val pathParams = path.split(',')
                val x = pathParams[0].toInt()
                val y = pathParams[1].toInt()
                return@m2 Node(x, y)
            }.toList()
        }

        val minX = rockPaths.flatten().map { it.x }.minOrNull()!!
        val maxX = rockPaths.flatten().map { it.x }.maxOrNull()!!
        val minY = 0
        val maxY = rockPaths.flatten().map { it.y }.maxOrNull()!!

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                tiles[Node(x, y)] = Tile.AIR
            }
        }

        rockPaths.forEach { rockPath ->
            rockPath.windowed(2).forEach { nodes ->
                val first = nodes[0]
                val second = nodes[1]

                when {
                    first.x == second.x -> {
                        val x = first.x
                        val step = if (first.y > second.y) -1 else 1

                        if (step > 0) {
                            for (y in first.y..second.y) {
                                tiles[Node(x, y)] = Tile.ROCK
                            }
                        } else {
                            for (y in first.y downTo second.y) {
                                tiles[Node(x, y)] = Tile.ROCK
                            }
                        }
                    }

                    first.y == second.y -> {
                        val y = first.y
                        val step = if (first.x > second.x) -1 else 1

                        if (step > 0) {
                            for (x in first.x..second.x) {
                                tiles[Node(x, y)] = Tile.ROCK
                            }
                        } else {
                            for (x in first.x downTo second.x) {
                                tiles[Node(x, y)] = Tile.ROCK
                            }
                        }
                    }
                }
            }
        }
        return tiles
    }

    private fun printTiles(tiles: Map<Node, Tile>) {
        val minX = tiles.keys.map { it.x }.minOrNull()!!
        val maxX = tiles.keys.map { it.x }.maxOrNull()!!
        val minY = 0
        val maxY = tiles.keys.map { it.y }.maxOrNull()!!

        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                print(String.format("%-3s", tiles[Node(x, y)]!!.c))
            }
            print("\n")
        }
    }
}
