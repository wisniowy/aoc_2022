import aoc.days.*
import org.reflections.Reflections
import kotlin.system.measureTimeMillis

data class Result(val result: Any, val measuredTime: Long)

private const val ROW_FORMAT = "%-10s %-15s %-15s %-15s %s"

fun main(args: Array<String>) {
    val reflections = Reflections("aoc.days")

    println(String.format(ROW_FORMAT, "Day", "Part One", "Time", "Part Two", "Time"))
    if (args.isEmpty()) {
        reflections.getSubTypesOf(SolutionDay::class.java)
            .map { it.constructors[0].newInstance() as SolutionDay}
            .sortedBy { it.day_n }
            .forEach { solve(it) }
    } else {
        val day_n = args[0].toInt()
        reflections.getSubTypesOf(SolutionDay::class.java)
            .filter { it.simpleName.equals("SolutionDay$day_n") }
            .map { it.constructors[0].newInstance() as SolutionDay}
            .map { solve(it) }
    }
}

fun solve(sol: SolutionDay) {
    val partOneResult = measureTime { sol.partOne() }
    val partTwoResult = measureTime { sol.partTwo() }

    println(String.format(ROW_FORMAT, sol.day_n,
        if (partOneResult.result.toString().length <= 15) partOneResult.result.toString() else "<TLDR>",
        "${partOneResult.measuredTime} ms",
        if (partTwoResult.result.toString().length <= 15) partTwoResult.result.toString() else "<TLDR>",
        "${partTwoResult.measuredTime} ms"))
}

fun measureTime(solutionFunction: () -> Any): Result {
    lateinit var result: Any
    val time = measureTimeMillis {
        result = solutionFunction.invoke()
    }

    return Result(result, time)
}