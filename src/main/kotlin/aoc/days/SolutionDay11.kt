package aoc.days

import aoc.utils.Reader

/*
    https://www.khanacademy.org/computing/computer-science/cryptography/modarithmetic/a/modular-addition-and-subtraction

    Modular addition:
    (A + B) mod C = (A mod C + B mod C) mod C

    Modular multiplication:
    (A * B) mod C = (A mod C * B mod C) mod C
 */
class SolutionDay11 : SolutionDay(11) {

    abstract class Expression {
        var value: Long? = null
        val reminders = mutableMapOf<Long, Long>()

        abstract fun getVal(): Long
        abstract fun rem(divisor: Long): Long
    }

    class AdditionExpression(val expr1: Expression, val expr2: Expression) : Expression() {
        override fun getVal(): Long {
            return expr1.getVal() + expr2.getVal()
        }

        override fun rem(divisor: Long): Long {
            return reminders.computeIfAbsent(divisor) { (expr1.rem(divisor) + expr2.rem(divisor)) % divisor }
        }
    }

    class MultiplicationExpression(val expr1: Expression, val expr2: Expression) : Expression() {
        override fun getVal(): Long {
            return expr1.getVal() * expr2.getVal()
        }

        override fun rem(divisor: Long): Long {
            return reminders.computeIfAbsent(divisor) { (expr1.rem(divisor) * expr2.rem(divisor)) % divisor }
        }
    }

    private class ValueExpression(val v: Long) : Expression() {
        override fun getVal(): Long {
            return v
        }

        override fun rem(divisor: Long): Long {
            return v % divisor
        }
    }

    data class Operation(
        val sign: Char,
        val first: Long?,
        val second: Long?,
        val func: (Expression, Expression) -> Expression
    )

    data class Test(val divisor: Long, val ifTrueMonkey: Int, val ifFalseMonkey: Int)
    data class Monkey(val monkeyId: Int, val items: ArrayDeque<Expression>, val operation: Operation, val test: Test)

    companion object {
        val MONKEY_REGEX =
            "^Monkey (\\d): Starting items: ([0-9 ,]+) Operation: new = ([old*+0-9 ]+) Test: divisible by (\\d+) If true: throw to monkey (\\d) If false: throw to monkey (\\d+)\$".toRegex()
    }

    fun Char.toOperationFunc(): (Expression, Expression) -> Expression {
        when (this) {
            '*' -> return { a, b -> MultiplicationExpression(a, b) }
            '+' -> return { a, b -> AdditionExpression(a, b) }
            else -> error("illegal args")
        }
    }

    override fun partOne(): Any {
        return monkeyBusiness(20, 3)
    }

    override fun partTwo(): Any {
        return monkeyBusiness(10000, null)
    }

    private fun monkeyBusiness(rounds: Int, worryLevelDivisor: Int?): Long {
        val monkeys = parseInput(Reader.inputAsString(day_n))
        val monkeysInspections: MutableMap<Int, Long> = monkeys.map { it.value.monkeyId to 0L }.toMap().toMutableMap()

        repeat(rounds) {
            monkeys.forEach { id, monkey ->
                val items = monkey.items
                val operation = monkey.operation
                val test = monkey.test

                while (items.isNotEmpty()) {
                    val item = items.removeFirst()
                    monkeysInspections[monkey.monkeyId] = monkeysInspections[monkey.monkeyId]!! + 1

                    val firstExpression = if (operation.first == null) item else ValueExpression(operation.first)
                    val secondExpression = if (operation.second == null) item else ValueExpression(operation.second)
                    var new: Expression = operation.func.invoke(firstExpression, secondExpression)
                    new = if (worryLevelDivisor != null) ValueExpression(new.getVal() / worryLevelDivisor) else new

                    val newMonkeyId = if (new.rem(test.divisor) == 0L) test.ifTrueMonkey else test.ifFalseMonkey

                    monkeys[newMonkeyId]!!.items.add(new)
                }
            }
        }

        return monkeysInspections.values.sorted().takeLast(2).reduce { first, second -> first * second }
    }

    private fun parseInput(input: String): LinkedHashMap<Int, Monkey> {
        val monkeys = LinkedHashMap<Int, Monkey>()

        input.split("\n\n").forEach { monkey ->
            val monkeyMatch = MONKEY_REGEX.find(
                monkey.replace("\n", "").replace(" +".toRegex(), " ")
            )!!.destructured

            val monkeyId = monkeyMatch.component1().toInt()
            val items =
                monkeyMatch.component2().split(", ").map { it.toLong() }.map { ValueExpression(it) }.toMutableList()
            val operationStr = monkeyMatch.component3().split(" ")
            val divisor = monkeyMatch.component4().toLong()
            val testTrueMonkeyId = monkeyMatch.component5().toInt()
            val testFalseMonkeyId = monkeyMatch.component6().toInt()
            val firstOperand: Long? = if (operationStr[0].equals("old")) null else operationStr[0].toLong()
            val secondOperand: Long? = if (operationStr[2].equals("old")) null else operationStr[2].toLong()
            val op = operationStr[1][0].toOperationFunc()

            val test = Test(divisor, testTrueMonkeyId, testFalseMonkeyId)
            val operation = Operation(operationStr[1][0], firstOperand, secondOperand, op)

            monkeys.put(monkeyId, Monkey(monkeyId, ArrayDeque(items), operation, test))
        }

        return monkeys
    }
}
