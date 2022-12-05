import aoc.days.*
import org.reflections.Reflections

fun main(args: Array<String>) {
    val day_n = 5
    val reflections = Reflections("aoc.days")

    val sol = reflections.getSubTypesOf(SolutionDay::class.java)
        .filter { it.simpleName.equals("SolutionDay$day_n") }[0].constructors[0].newInstance() as SolutionDay

    println("Part one: ${sol.partOne()}")
    println("Part two: ${sol.partTwo()}")
}