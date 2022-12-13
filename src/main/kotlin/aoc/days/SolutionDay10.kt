package aoc.days

import aoc.utils.Reader
import kotlin.math.abs

class SolutionDay10 : SolutionDay(10) {

    private enum class InstructionType(val cycles: Int) {
        noop(1),
        addx(2)
    }

    private data class Instruction(val instructionType: InstructionType, val value: Int)


    override fun partOne(): Any {
        val instructions = parseInput(Reader.inputAsStringList(day_n))
        var registerX = 1
        var currentCycle = 1
        var dueCycle = 1
        var currentInstruction: Instruction? = null
        val results = (20..220 step 40).map { it to 0 }.toMap().toMutableMap()

        while (instructions.isNotEmpty()) {
            if (dueCycle == currentCycle) {
                if (currentInstruction != null && currentInstruction.instructionType == InstructionType.addx) {
                    registerX += currentInstruction.value
                }

                currentInstruction = instructions.removeFirst()
                dueCycle = currentCycle + currentInstruction.instructionType.cycles
            }

            if (currentCycle in results.keys) {
                results[currentCycle] = currentCycle * registerX
            }

            currentCycle += 1
        }

        return results.values.sum()
    }

    override fun partTwo(): Any {
        val instructions = parseInput(Reader.inputAsStringList(day_n))
        var registerX = 1
        var currentCycle = 0
        var dueCycle = 0
        var currentInstruction: Instruction? = null
        val crtScreen = (0..200 step 40).map { Array<Char>(40) {i -> '.'} }.toTypedArray()

        while (instructions.isNotEmpty()) {
            if (dueCycle == currentCycle) {
                if (currentInstruction != null && currentInstruction.instructionType == InstructionType.addx) {
                    registerX += currentInstruction.value
                }

                currentInstruction = instructions.removeFirst()
                dueCycle = currentCycle + currentInstruction.instructionType.cycles
            }
            val row: Int = (currentCycle) / 40

            if ((currentCycle % 40) in setOf(registerX % 40 - 1, registerX % 40, registerX % 40 + 1) ) {
                crtScreen[row][currentCycle % 40] = '#'
            } else {
                crtScreen[row][currentCycle % 40] = '.'
            }

            currentCycle += 1
        }

        println(crtScreen.map { it.joinToString("")}.joinToString("\n"))
        return crtScreen.map { it.joinToString("")}.joinToString("\n")
    }

    private fun parseInput(lines: List<String>): ArrayDeque<Instruction> {
        return ArrayDeque(lines.map {
            val line = it.split(" ")
            val instructionType = InstructionType.valueOf(line[0])

            when (instructionType) {
                InstructionType.noop -> return@map Instruction(instructionType, 0)
                InstructionType.addx -> return@map Instruction(instructionType, line[1].toInt())
            }
        })
    }
}
