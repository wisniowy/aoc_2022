package aoc.days

import aoc.utils.Reader
import aoc.utils.Tree

private const val MAX_DISK_SPACE = 70000000
private const val UPDATE_SPACE_NEEDED = 30000000

class SolutionDay7 : SolutionDay(7) {

    private data class File(val name: String, val size: Long)
    private data class Directory(val name: String, val files: MutableList<File>)

    private val getFilesSize: (Tree.TreeNode<Directory>) -> Long = { it.value.files.map { it.size }.sum() }
    private val longSum: (Long, Long) -> Long = { v1: Long, v2: Long -> v1 + v2 }

    override fun partOne(): Any {
        val dirTree: Tree<Directory> = readDirectoryTree()

        return dirTree.getRoot().forEachDepthFirst(
            visit = v1@{ node: Tree.TreeNode<Directory> ->
                val result = node.forEachDepthFirst(
                    visit = getFilesSize,
                    agg = longSum
                )
                return@v1 if (result <= 100000) result else 0L
            },
            agg = longSum
        )
    }


    override fun partTwo(): Any {
        val dirTree: Tree<Directory> = readDirectoryTree()

        val dirsSizes = dirTree.getRoot().forEachDepthFirst(
            visit = v1@{ node ->
                val result = node.forEachDepthFirst(
                    visit = getFilesSize,
                    agg = longSum
                )
                return@v1 mutableListOf(result)
            },
            agg = a1@{ v1: MutableList<Long>, v2: MutableList<Long> ->
                v1.addAll(v2)
                return@a1 v1
            }
        )

        val usedSpace = dirsSizes.maxOrNull() ?: error("no dirs")
        val neededSpace = UPDATE_SPACE_NEEDED - (MAX_DISK_SPACE - usedSpace)
        return dirsSizes.filter { it >= neededSpace }.minOrNull() ?: error("no dirs")
    }

    private fun readDirectoryTree(): Tree<Directory> {
        val rootDir = Directory("/", mutableListOf())

        val dirTree: Tree<Directory> = Tree(Tree.TreeNode(rootDir))
        var currentNode = dirTree.getRoot()

        Reader.inputAsStringList(day_n).forEach { line ->
            when {
                line.startsWith("$ ls") -> {}

                line.startsWith("$ cd") -> {
                    val commandLine = line.trim().split(' ')
                    when (commandLine[2]) {
                        ".." -> currentNode = currentNode.parent ?: dirTree.getRoot()
                        "/" -> currentNode = dirTree.getRoot()
                        else -> {
                            if (currentNode.children.filter { it.value.name == commandLine[2] }.isEmpty()) {
                                val newDir = Directory(commandLine[1], mutableListOf())
                                currentNode.add(Tree.TreeNode(newDir))
                            }
                            currentNode = currentNode.children.filter { it.value.name == commandLine[2] }.first()
                        }
                    }
                }

                line.startsWith("dir") -> {
                    val commandLine = line.trim().split(' ')
                    val newDir = Directory(commandLine[1], mutableListOf())
                    currentNode.add(Tree.TreeNode(newDir, currentNode))
                }

                else -> {
                    val commandLine = line.trim().split(' ')
                    val fileSize = commandLine[0].toLong()
                    val fileName = commandLine[1]
                    val file = File(fileName, fileSize)
                    currentNode.value.files.add(file)
                }
            }
        }

        return dirTree
    }
}
