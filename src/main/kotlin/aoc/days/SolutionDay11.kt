package aoc.days

import aoc.utils.Reader

class SolutionDay11 : SolutionDay(11) {

    data class Operation(val first: Long?, val second: Long?, val func: (Long, Long) -> Long)
    data class Test(val divisor: Long, val ifTrueMonkey: Int, val ifFalseMonkey: Int)
    data class Monkey(val monkeyId: Int, val items: ArrayDeque<Long>, val operation: Operation, val test: Test)

    companion object {
        val MONKEY_REGEX = "^Monkey (\\d): Starting items: ([0-9 ,]+) Operation: new = ([old*+0-9 ]+) Test: divisible by (\\d+) If true: throw to monkey (\\d) If false: throw to monkey (\\d+)\$".toRegex()
    }

    fun Char.toOperationFunc(): (Long, Long) -> Long {
        when (this) {
            '*' -> return { a, b -> a * b }
            '+' -> return { a, b -> a + b }
            else -> error("illegal args")
        }
    }
    override fun partOne(): Any {
        val rounds = 20

        return monkeyBusiness(rounds, 3)
    }

    private fun monkeyBusiness(rounds: Int, worryLevelDivisor: Int): Long {
        val monkeys = parseInput(Reader.inputAsString(day_n))
        val monkeysInspections: MutableMap<Int, Long> = monkeys.map { it.value.monkeyId to 0L }.toMap().toMutableMap()

        repeat(rounds) {
            monkeys.forEach { id, monkey ->
                val items = monkey.items
                val operation = monkey.operation
                val test = monkey.test


                while (items.isNotEmpty()) {
                    val item = items.removeFirst()
                    monkeysInspections[monkey.monkeyId] = monkeysInspections[monkey.monkeyId]!! + 1L

                    val firstOperand = if (operation.first == null) item else operation.first
                    val secondOperand = if (operation.second == null) item else operation.second
                    val new: Long = operation.func.invoke(firstOperand, secondOperand) / worryLevelDivisor

                    val newMonkeyId = if (new % test.divisor == 0L) test.ifTrueMonkey else test.ifFalseMonkey

                    monkeys[newMonkeyId]!!.items.add(new)
                }
            }
        }


        return monkeysInspections.values.sorted().takeLast(2).reduce { first, second -> first * second }
    }

    override fun partTwo(): Any {
        return monkeyBusiness(10000, 1)
    }

    private fun parseInput(input: String): LinkedHashMap<Int, Monkey> {
        val monkeys = LinkedHashMap<Int, Monkey>()

        input.split("\n\n").forEach { monkey ->
            val monkeyMatch = MONKEY_REGEX.find(monkey.replace("\n", "")
                .replace(" +".toRegex(), " "))!!.destructured

            val monkeyId = monkeyMatch.component1().toInt()
            val items = monkeyMatch.component2().split(", ").map { it.toLong() }.toMutableList()
            val operationStr = monkeyMatch.component3().split(" ")
            val divisor = monkeyMatch.component4().toLong()
            val testTrueMonkeyId = monkeyMatch.component5().toInt()
            val testFalseMonkeyId = monkeyMatch.component6().toInt()
            val firstOperand: Long? = if (operationStr[0].equals("old")) null else operationStr[0].toLong()
            val secondOperand: Long? = if (operationStr[2].equals("old")) null else operationStr[2].toLong()
            val op = operationStr[1][0].toOperationFunc()

            val test = Test(divisor, testTrueMonkeyId, testFalseMonkeyId)
            val operation = Operation(firstOperand, secondOperand, op)


            monkeys.put(monkeyId, Monkey(monkeyId, ArrayDeque(items), operation, test))
        }

        return monkeys
    }
}
