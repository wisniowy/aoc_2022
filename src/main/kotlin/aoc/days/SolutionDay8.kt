package aoc.days

import aoc.utils.Reader

class SolutionDay8 : SolutionDay(8) {

    private data class Direction(val xStart: Int?, val xEnd: Int?, val yStart: Int?, val yEnd: Int?, val step: Int)

    override fun partOne() : Any {
        val treeMatrix = parseMatrix(Reader.inputAsStringList(day_n))
        val height = treeMatrix.size
        val width = treeMatrix[0].size

        val seenTreesMatrix = (1..height).map { BooleanArray(width)}

        val directions = listOf(
            Direction(0, width - 1, null, null, step = 1),
            Direction(width - 1, 0, null,null, step = -1),
            Direction(null, null, 0, height - 1, step = 1),
            Direction(null, null, height - 1, 0, step = -1)
        )

        for (direction in directions) {
            if (direction.xStart != null) {
                val xStart = direction.xStart
                val xEnd = direction.xEnd

                for (y in 0..(height - 1)) {
                    var x = xStart
                    val prevTreesHeights: MutableList<Int> = mutableListOf()
                    while (x != xEnd!! + direction.step) {
                        if (prevTreesHeights.filter { it >= treeMatrix[y][x] }.size == 0) {
                            seenTreesMatrix[y][x] = true
                        }

                        prevTreesHeights.add(treeMatrix[y][x])
                        x += direction.step
                    }
                }
                continue
            }

            if (direction.yStart != null) {
                val yStart = direction.yStart
                val yEnd = direction.yEnd

                for (x in 0..(width - 1)) {
                    var y = yStart
                    val prevTreesHeights: MutableList<Int> = mutableListOf()
                    while (y != yEnd!! + direction.step) {
                        if (prevTreesHeights.filter { it >= treeMatrix[y][x] }.size == 0) {
                            seenTreesMatrix[y][x] = true
                        }

                        prevTreesHeights.add(treeMatrix[y][x])
                        y += direction.step
                    }
                }
                continue
            }
        }

        return seenTreesMatrix.map { it.count { it } }.sum()
    }

    override fun partTwo() : Any {
        val treeMatrix = parseMatrix(Reader.inputAsStringList(day_n))
        val height = treeMatrix.size
        val width = treeMatrix[0].size

        val scenicScoresMatrix = (1..height).map { IntArray(width) { i -> 1 } }

        val directions = listOf(
            Direction(0, width - 1, null, null, step = 1),
            Direction(width - 1, 0, null,null, step = -1),
            Direction(null, null, 0, height - 1, step = 1),
            Direction(null, null, height - 1, 0, step = -1)
        )

        treeMatrix.indices.forEach { yStart ->
            treeMatrix[0].indices.forEach { xStart ->
                for (direction in directions) {
                    var seenTrees = 0

                    if (direction.xEnd != null) {
                        val xEnd = direction.xEnd
                        var x = xStart + direction.step

                        while (x != xEnd + direction.step) {
                            seenTrees += 1
                            if (treeMatrix[yStart][xStart] <= treeMatrix[yStart][x]) break
                            x += direction.step
                        }

                        scenicScoresMatrix[yStart][xStart] *= seenTrees
                        continue
                    }

                    if (direction.yEnd != null) {
                        val yEnd = direction.yEnd
                        var y = yStart + direction.step

                        while (y != yEnd + direction.step) {
                            seenTrees += 1
                            if (treeMatrix[yStart][xStart] <= treeMatrix[y][xStart]) break
                            y += direction.step
                        }

                        scenicScoresMatrix[yStart][xStart] *= seenTrees
                        continue
                    }
                }

            }
        }


        return scenicScoresMatrix.map { it.maxOrNull() }.filterNotNull().maxOrNull() ?: error("illegal state")
    }

    private fun parseMatrix(lines: List<String>) : Array<IntArray> {
        return lines.map { it.toCharArray() }
            .map { it.map { it.digitToInt() }.toIntArray() }
            .toTypedArray()
    }
}
