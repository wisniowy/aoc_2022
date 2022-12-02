package aoc.utils

import java.io.File
import java.io.FileNotFoundException

object Reader {

    fun inputAsString(day_n: Int) : String {
        return loadFileFromResources(day_n).readText()
    }

    fun inputAsStringList(day_n: Int) : List<String> {
        return loadFileFromResources(day_n).readLines()
    }

    fun loadFileFromResources(day_n: Int): File {
        return File(javaClass.classLoader.getResource("input_day_$day_n.txt")?.toURI()
            ?: throw FileNotFoundException("File does not exist"))
    }
}