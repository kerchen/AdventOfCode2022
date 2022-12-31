package day20

import readInput

fun parseInput(input: List<String>): List<Int> {
    var parsed = mutableListOf<Int>()

    for (i in input) {
        parsed.add(i.toInt())
    }

    // Numbers are unique, right?
    check(parsed.toSet().size == parsed.size)

    return parsed
}

fun findIndex(value: Int, mixedFile: List<Int>): Int = mixedFile.indexOfFirst {it == value}


fun findGroveCoordinate(mixedFile: List<Int>, offset: Int): Int {
    val pivot = findIndex(0, mixedFile)
    var index = (pivot + offset) % mixedFile.size

    return mixedFile[index]
}

fun mix(value: Int, mixedFile: MutableList<Int>) {
    val oldIndex = findIndex(value, mixedFile)
    var newIndex = (oldIndex + value + mixedFile.size) % mixedFile.size

    if (oldIndex == newIndex) return

    if (value > 0 && oldIndex > newIndex) {
        newIndex = (newIndex + 1) % mixedFile.size
    }
    if (value < 0 && oldIndex < newIndex) {
        newIndex = (newIndex - 1 + mixedFile.size) % mixedFile.size
    }
    mixedFile.removeAt(oldIndex)
    mixedFile.add(newIndex, value)
}


fun main() {
    fun part1(input: List<String>): Int = 0

    fun part2(input: List<String>): Int = 0

    val testInput = readInput("Day20_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 0)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
