package aoc.days

import aoc.utils.Reader
import kotlin.math.max


class SolutionDay16 : SolutionDay(16) {

    data class Valve(val name: String, val rate: Int, val nextValves: MutableList<Valve>, var opened: Boolean = false)
    data class ValveState(val name: String, val minute: Int, val opened: Set<String>)

    companion object {
        val VALVE_REGEX = "^Valve ([A-Z]+) has flow rate=(\\d+); tunnels? leads? to valves? ([A-Z, ]+)$".toRegex()
    }

    private fun visitValve(valve: Valve,
                           remainingMinutes: Int,
                           remainingValves: Set<String>,
                           valveDistances: Map<String, Map<String, Int>>,
                           cache: MutableMap<ValveState, Int>,
                           valves: Map<String, Valve>,
                           ifElephantHelp: Boolean = false): Int {

        val currentValveState = ValveState(valve.name, remainingMinutes, remainingValves)

        return remainingMinutes * valve.rate + cache.getOrPut(currentValveState) {
            val maxScore = valveDistances[valve.name]!!
                .filter { remainingValves.contains(it.key) }
                .filter { it.value <= remainingMinutes }
                .map { (nextValve, distance) -> visitValve(valves[nextValve]!!,
                    remainingMinutes - distance,
                remainingValves - nextValve,
                    valveDistances,
                    cache,
                    valves, ifElephantHelp)
                }.maxOrNull() ?: 0
            val elephantHelpMaxScore = if (ifElephantHelp) visitValve(valves["AA"]!!,
            26, remainingValves, valveDistances, mutableMapOf(), valves) else 0
            return@getOrPut max(maxScore, elephantHelpMaxScore)
        }
    }

    private fun calculateDistances(valves: List<Valve>): Map<String, Map<String, Int>> {
        return valves.map { valve ->
            val valveDistances = mutableMapOf<String, Int>().withDefault { Int.MAX_VALUE }.apply { put(valve.name, 1) }
            val nonVisitedValves = mutableListOf(valve)
            while (nonVisitedValves.isNotEmpty()) {
                val currentValve = nonVisitedValves.removeFirst()
                currentValve.nextValves.forEach { nextValve ->
                    val dist = valveDistances.getValue(currentValve.name) + 1
                    if (dist < valveDistances.getValue(nextValve.name)) {
                        valveDistances[nextValve.name] = dist
                        nonVisitedValves.add(nextValve)
                    }
                }
            }
            return@map valve.name to valveDistances
        }.toMap()
    }
    override fun partOne(): Any {
        val valves = parseInput(Reader.inputAsStringList(day_n))
        val startValve = valves["AA"]!!
        val distances = calculateDistances(valves.values.toList())

        val cache = mutableMapOf<ValveState, Int>()
        return visitValve(startValve, 30,
            valves.values.filter { it.rate != 0 }.map { it.name }.toSet(), distances, cache, valves)
    }

    override fun partTwo(): Any {
        val valves = parseInput(Reader.inputAsStringList(day_n))
        val startValve = valves["AA"]!!
        val distances = calculateDistances(valves.values.toList())

        val cache = mutableMapOf<ValveState, Int>()
        return visitValve(startValve, 26,
            valves.values.filter { it.rate != 0 }.map { it.name }.toSet(), distances, cache, valves, ifElephantHelp = true)
    }

    private fun parseInput(lines: List<String>): LinkedHashMap<String, Valve> {
        val valves = linkedMapOf<String, Valve>()
        val nextValvesMap = mutableMapOf<String, List<String>>()

        lines.forEach { line ->
            val valveMatch = VALVE_REGEX.find(line)
            val (valve, valveRate, nextValvesList) = valveMatch!!.destructured
            valves[valve] = Valve(valve, valveRate.toInt(), mutableListOf())
            nextValvesMap[valve] = nextValvesList.replace(" ", "").split(',').toList()
        }

        valves.forEach { (valveName, valve) ->
            val nextValves = nextValvesMap.getOrDefault(valveName, listOf()).map { name -> valves[name]!! }.toList()
            valve.nextValves.addAll(nextValves)
        }

        return valves
    }
}
