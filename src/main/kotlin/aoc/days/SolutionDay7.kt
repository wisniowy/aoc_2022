package aoc.days

import aoc.utils.Reader
import aoc.utils.Tree

class SolutionDay7 : SolutionDay(7) {

    data class File(val name: String, val size: Long)
    data class Directory(val name: String, val files: MutableList<File>)

    override fun partOne(): Any {
        val dirs = mutableMapOf<String, Directory>()
        val files = mutableMapOf<String, File>()

        val rootDir = Directory("/", mutableListOf())
        dirs[rootDir.name] = rootDir

        val dirTree: Tree<Directory> = Tree(Tree.TreeNode(rootDir))
        var currentNode = dirTree.getRoot()

        Reader.inputAsStringList(day_n).forEach { line ->
            if (line.startsWith('$')) {
                val commandLine = line.replace("$", "").trim().split(' ')
                when (commandLine[0]) {
                    "cd" -> {
                        when (commandLine[1]) {
                            ".." -> currentNode = currentNode.parent ?: dirTree.getRoot()
                            "/" -> currentNode = dirTree.getRoot()
                            else -> {
                                if (!dirs.contains(commandLine[1])) {
                                    val newDir = Directory(commandLine[1], mutableListOf())
                                    dirs[newDir.name] = newDir
                                    currentNode.add(Tree.TreeNode(newDir))
                                }
                                currentNode = currentNode.children.filter { it.value.name == commandLine[1] }.first()
                            }
                        }

                    }
                }
            } else {
                val listLine = line.trim().split(' ')

                when (listLine[0]) {
                    "dir" -> {
                        val newDir = Directory(listLine[1], mutableListOf())
                        dirs[newDir.name] = newDir
                        currentNode.add(Tree.TreeNode(newDir, currentNode))
                    }

                    else -> {
                        val fileSize = listLine[0].toLong()
                        val fileName = listLine[1]
                        val file = File(fileName, fileSize)
                        files[fileName] = file
                        currentNode.value.files.add(file)
                    }
                }
            }
        }

        return dirTree.getRoot().forEachDepthFirst(
            visit = v1@{ node ->
                val result = node.forEachDepthFirst(
                    visit = v@{
                        val aggSum = it.value.files.map { it.size }.sum()
                        return@v aggSum
                    },
                    agg = { v1: Long, v2: Long -> v1 + v2 }
                )
                return@v1 if (result <= 100000) result else 0L
            },
            agg = { v1: Long, v2: Long -> v1 + v2 }
        )
    }

    override fun partTwo(): Any {
        val dirs = mutableMapOf<String, Directory>()
        val files = mutableMapOf<String, File>()

        val rootDir = Directory("/", mutableListOf())
        dirs[rootDir.name] = rootDir

        val dirTree: Tree<Directory> = Tree(Tree.TreeNode(rootDir))
        var currentNode = dirTree.getRoot()

        Reader.inputAsStringList(day_n).forEach { line ->
            if (line.startsWith('$')) {
                val commandLine = line.replace("$", "").trim().split(' ')
                when (commandLine[0]) {
                    "cd" -> {
                        when (commandLine[1]) {
                            ".." -> currentNode = currentNode.parent ?: dirTree.getRoot()
                            "/" -> currentNode = dirTree.getRoot()
                            else -> {
                                if (!dirs.contains(commandLine[1])) {
                                    val newDir = Directory(commandLine[1], mutableListOf())
                                    dirs[newDir.name] = newDir
                                    currentNode.add(Tree.TreeNode(newDir))
                                }
                                currentNode = currentNode.children.filter { it.value.name == commandLine[1] }.first()
                            }
                        }

                    }
                }
            } else {
                val listLine = line.trim().split(' ')

                when (listLine[0]) {
                    "dir" -> {
                        val newDir = Directory(listLine[1], mutableListOf())
                        dirs[newDir.name] = newDir
                        currentNode.add(Tree.TreeNode(newDir, currentNode))
                    }

                    else -> {
                        val fileSize = listLine[0].toLong()
                        val fileName = listLine[1]
                        val file = File(fileName, fileSize)
                        files[fileName] = file
                        currentNode.value.files.add(file)
                    }
                }
            }
        }

        val dirsSizes = dirTree.getRoot().forEachDepthFirst(
            visit = v1@{ node ->
                val result = node.forEachDepthFirst(
                    visit = v@{
                        val aggSum = it.value.files.map { it.size }.sum()
                        return@v aggSum
                    },
                    agg = { v1: Long, v2: Long -> v1 + v2 }
                )
                return@v1 mutableListOf(result)
            },
            agg = a1@{ v1: MutableList<Long>, v2: MutableList<Long> ->
                v1.addAll(v2)
                return@a1 v1
             }
        )

        val usedSpace = dirsSizes.maxOrNull() ?: error("no dirs")
        val neededSpace = 30000000 - (70000000 - usedSpace)
        return dirsSizes.filter { it >= neededSpace }.minOrNull() ?: error("no dirs")
    }
}
