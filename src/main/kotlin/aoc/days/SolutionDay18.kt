package aoc.days

import aoc.utils.Reader


class SolutionDay18 : SolutionDay(18) {

    data class Cube(val x: Int, val y: Int, val z: Int)
    data class Neighbour(val x: Int, val y: Int, val z: Int)
    private enum class ObjectType { LAVA, EMPTY, WATER}

    companion object {
        val NEIGHBOURS = listOf<Neighbour>(
            Neighbour(1, 0, 0),
            Neighbour(-1, 0, 0),
            Neighbour(0, 1, 0),
            Neighbour(0, -1, 0),
            Neighbour(0, 0, 1),
            Neighbour(0, 0, -1),
        )
    }

    private fun floodFill(currentCube: Cube, space: MutableMap<Cube, ObjectType>) {
        space[currentCube] = ObjectType.WATER

        NEIGHBOURS.forEach { n ->
            val nextCube =  Cube(currentCube.x + n.x, currentCube.y + n.y, currentCube.z + n.z)
            if (space.containsKey(nextCube) && space[nextCube] == ObjectType.EMPTY) {
                floodFill(nextCube, space)
            }
        }
    }

    override fun partOne(): Any {
        val cubes = parseInput(Reader.inputAsStringList(day_n))

        val neighbours = cubes.map m1@{ cube ->
            NEIGHBOURS.map m2@ { n ->
                val potentialNeighbour = Cube(cube.x + n.x, cube.y + n.y, cube.z + n.z)
                return@m2 when {
                    cubes.contains(potentialNeighbour) -> setOf(cube, potentialNeighbour)
                    else -> null
                }
            }.filterNotNull()
        }.flatten().toSet()

        return 6 * cubes.size - 2 * neighbours.size
    }

    override fun partTwo(): Any {
        val cubes = parseInput(Reader.inputAsStringList(day_n))

        val xRange = (cubes.map { it.x }.minOrNull()!! - 1..cubes.map { it.x }.maxOrNull()!! + 1)
        val yRange = (cubes.map { it.y }.minOrNull()!! - 1..cubes.map { it.y }.maxOrNull()!! + 1)
        val zRange = (cubes.map { it.z }.minOrNull()!! - 1..cubes.map { it.z }.maxOrNull()!! + 1)

        val space = mutableMapOf<Cube, ObjectType>()

        xRange.forEach { x ->
            yRange.forEach { y ->
                zRange.forEach { z ->
                    val cube = Cube(x, y, z)
                    space[cube] = if (cubes.contains(cube)) ObjectType.LAVA else ObjectType.EMPTY
                }
            }
        }

        floodFill(Cube(xRange.first, yRange.first, zRange.first), space)

        val waterCubes = space.filter { it.value == ObjectType.WATER }.map { it.key }

        return waterCubes.map m1@{ cube ->
            NEIGHBOURS.map m2@ { n ->
                val potentialNeighbour = Cube(cube.x + n.x, cube.y + n.y, cube.z + n.z)
                return@m2 when {
                    cubes.contains(potentialNeighbour) -> setOf(cube, potentialNeighbour)
                    else -> null
                }
            }.filterNotNull()
        }.flatten().toSet().size
    }

    private fun parseInput(lines: List<String>): Set<Cube> {
        return lines.map { line ->
            val (x, y, z) = line.split(",")
            return@map Cube(x.toInt(), y.toInt(), z.toInt())
        }.toSet()
    }
}
