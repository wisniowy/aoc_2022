package aoc.days

import aoc.utils.Reader

class SolutionDay5 : SolutionDay(5) {


    data class Instruction(val qty: Int, val from: Int, val to: Int)

    val instructionRegex = "^move +(\\d+) +from +(\\d+) +to +(\\d+)$".toRegex()
    val crateLineRegex = "(\\[[A-Z]]| {3}) {0,1}".toRegex()

    fun <T> ArrayDeque<T>.removeLast(n: Int): List<T> {
        val removed = mutableListOf<T>()
        repeat(n) {
            if (this.isNotEmpty()) {
                removed.add(this.removeLast())
            }
        }
        return removed
    }

    override fun partOne() : Any {
        val parsedInput = parseInput(Reader.inputAsStringList(day_n))
        val instructions = parsedInput.first
        val stacks = parsedInput.second

        instructions.reversed().forEach { instruction ->
            val elementsToMove = stacks[instruction.from - 1].removeLast(instruction.qty)

            elementsToMove.forEach {
                stacks[instruction.to - 1].addLast(it)
            }
        }

        return stacks.map { it.lastOrNull() ?: " " }
            .joinToString("")
    }

    override fun partTwo() : Any {
        val parsedInput = parseInput(Reader.inputAsStringList(day_n))
        val instructions = parsedInput.first
        val stacks = parsedInput.second

        instructions.reversed().forEach { instruction ->
            val elementsToMove = stacks[instruction.from - 1].removeLast(instruction.qty)

            elementsToMove.reversed().forEach {
                stacks[instruction.to - 1].addLast(it)
            }
        }

        return stacks.map { it.lastOrNull() ?: " " }
            .joinToString("")
    }

    private fun parseInput(input: List<String>): Pair<List<Instruction>, List<ArrayDeque<Char>>> {
        val instructions = mutableListOf<Instruction>()
        val stacks = mutableListOf<ArrayDeque<Char>>()

        for (line in input.reversed()) {
            val instructionMatch = instructionRegex.find(line)

            if (instructionMatch != null) {
                val (qty, fromVal, toVal) = instructionMatch.destructured
                instructions.add(Instruction(qty.toInt(), fromVal.toInt(), toVal.toInt()))
                continue
            }

            if (line.isBlank() || !line.contains('[')) {
                continue
            }

            val crateLineMatches = crateLineRegex.findAll(line)
            crateLineMatches.map { it.groupValues[1].replace("(\\[|]| +)".toRegex(), "") }
                .forEachIndexed {  index, element ->
                    if (element.isBlank()) return@forEachIndexed

                    if (index > stacks.size - 1)
                        stacks.add(index, ArrayDeque())

                    stacks[index].addLast(element[0])
                }

        }

        return Pair(instructions, stacks)
    }
}
