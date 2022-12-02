package aoc.days

import aoc.utils.Reader

class SolutionDay1 {

    fun partOne() : Any {
        var maxCalories = 0
        var currentCalories = 0

        Reader.inputAsStringList(1)
            .map { it.toIntOrNull() }
            .forEach {
                if (it == null) {
                    maxCalories = maxOf(maxCalories, currentCalories)
                    currentCalories = 0
                    return@forEach
                }
                currentCalories += it
            }

        return maxCalories
    }

    fun partTwo() : Any {
        var firstLargestCalories = -1
        var secondLargestCalories = -2
        var thirdLargestCalories = -3

        var currentCalories = 0

        Reader.inputAsStringList(1)
            .map { it.toIntOrNull() }
            .forEach {
                if (it == null) {

                    if (currentCalories > firstLargestCalories) {
                        thirdLargestCalories = secondLargestCalories
                        secondLargestCalories = firstLargestCalories
                        firstLargestCalories = currentCalories
                    }
                    else if (currentCalories >= secondLargestCalories && currentCalories < firstLargestCalories) {
                        thirdLargestCalories = secondLargestCalories
                        secondLargestCalories = currentCalories
                    }
                    else if (currentCalories >= thirdLargestCalories && currentCalories < secondLargestCalories)
                        thirdLargestCalories = currentCalories

                    currentCalories = 0
                    return@forEach
                }
                currentCalories += it
            }

        return firstLargestCalories + secondLargestCalories + thirdLargestCalories
    }
}