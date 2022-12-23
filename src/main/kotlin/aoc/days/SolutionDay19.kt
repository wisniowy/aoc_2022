package aoc.days

import aoc.utils.Reader
import kotlin.math.max
import kotlin.math.min


class SolutionDay19 : SolutionDay(19) {

    companion object {
        val BLUEPRINT_REGEX = "Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.".toRegex()
    }

    enum class Robot(val resourcesPerMinute: Resources) {
        ORE(Resources(1, 0, 0, 0)),
        CLAY(Resources(0, 1, 0, 0)),
        OBSIDIAN(Resources(0, 0, 1, 0)),
        GEODE(Resources(0, 0, 0, 1))
    }

    data class Resources(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0, val geode: Int = 0)

    data class Blueprint(val robotsCosts: Map<Robot, Resources>)

    fun Resources.plus(resources: Resources): Resources {
        return Resources(
            ore = this.ore + resources.ore,
            clay = this.clay + resources.clay,
            obsidian = this.obsidian + resources.obsidian,
            geode = this.geode + resources.geode
        )
    }

    fun Resources.minus(resources: Resources): Resources {
        return Resources(
            ore = this.ore - resources.ore,
            clay = this.clay - resources.clay,
            obsidian = this.obsidian - resources.obsidian,
            geode = this.geode - resources.geode
        )
    }

    fun Resources.mul(value: Int): Resources {
        return Resources(
            ore = this.ore * value,
            clay = this.clay * value,
            obsidian = this.obsidian * value,
            geode = this.geode * value
        )
    }

    fun Resources.isEnough(resources: Resources): Boolean {
        return this.ore >= resources.ore
                && this.clay >= resources.clay
                && this.obsidian >= resources.obsidian
                && this.geode >= resources.geode
    }

    data class State(val remainingMinutes: Int, val currentResources: Resources, val oreRobots: Int, val clayRobots: Int, val obsidianRobots: Int, val geodeRobots: Int)

    private fun geodesHeuristics(remainingMinutes: Int, currentRobots: Map<Robot, Int>): Int {
        val sum = (0..remainingMinutes - 1).map { it + currentRobots.getOrDefault(Robot.GEODE, 0) }.sum()
        return sum
    }
    private fun useBlueprint(remainingMinutes: Int, currentRobots: Map<Robot, Int>, currentResources: Resources,
                             prevResources: Resources, blueprint: Blueprint, prevRobot: Robot?, maxGeodes: Int = 0): Int {
        if (remainingMinutes <= 0) return currentResources.geode

        if (geodesHeuristics(remainingMinutes, currentRobots) + currentResources.geode < maxGeodes) return currentResources.geode

        val newResources = currentResources.plus(currentRobots
            .map { (robot, count) -> robot.resourcesPerMinute.mul(count) }
            .reduce { r1, r2 -> r1.plus(r2) })

        var currentMaxGeodes = maxGeodes

        blueprint.robotsCosts.forEach { (robot, cost) ->
            val newCurrentRobots = currentRobots.toMutableMap().apply { put(robot, getOrDefault(robot, 0) + 1) }
            val geodes = when {
                prevRobot == null && prevResources.isEnough(cost) -> 0
                remainingMinutes > 1 && currentResources.isEnough(cost) -> useBlueprint(
                    remainingMinutes - 1,
                    newCurrentRobots,
                    newResources.minus(cost),
                    currentResources.copy(),
                    blueprint,
                    robot,
                currentMaxGeodes)
                else -> 0
            }
            currentMaxGeodes = max(currentMaxGeodes, geodes)
        }

        return max(
            currentMaxGeodes,
            useBlueprint(remainingMinutes - 1, currentRobots.toMutableMap(), newResources, currentResources, blueprint, null, currentMaxGeodes)
        )

    }

    override fun partOne(): Any {
        val blueprints = parseInput(Reader.inputAsStringList(day_n))
        val currentRobots = mutableMapOf( Robot.ORE to 1,
                        Robot.CLAY to 0,
        Robot.OBSIDIAN to 0,
        Robot.GEODE to 0)
        val currentResources = Resources()

        return blueprints.mapIndexed { index, blueprint ->
            (index + 1) * useBlueprint(24, currentRobots.toMap(), currentResources.copy(),currentResources.copy(), blueprint, null)
        }.sum()
    }

    override fun partTwo(): Any {
        val blueprints = parseInput(Reader.inputAsStringList(day_n))
        val currentRobots = mutableMapOf( Robot.ORE to 1,
            Robot.CLAY to 0,
            Robot.OBSIDIAN to 0,
            Robot.GEODE to 0)
        val currentResources = Resources()

        return blueprints.subList(0, min(3, blueprints.size)).map { blueprint -> useBlueprint(32, currentRobots.toMap(), currentResources.copy(),currentResources.copy(), blueprint, null)
        }.reduce { a, b -> a * b }
    }

    private fun parseInput(lines: List<String>): List<Blueprint> {
        return lines.map { line ->
            val blueprintMatch = BLUEPRINT_REGEX.find(line)
            val (id, oreRobotOre, clayRobotOre, obsidianRobotOre, obsidianRobotClay, geodeRobotOre, geodeRobotObsidian) =
                blueprintMatch!!.destructured
            return@map Blueprint(
                mutableMapOf(
                    Robot.ORE to Resources(ore = oreRobotOre.toInt()),
                    Robot.CLAY to Resources(ore = clayRobotOre.toInt()),
                    Robot.OBSIDIAN to Resources(ore = obsidianRobotOre.toInt(), clay = obsidianRobotClay.toInt()),
                    Robot.GEODE to Resources(ore = geodeRobotOre.toInt(), obsidian = geodeRobotObsidian.toInt())
                )
            )
        }
    }
}
