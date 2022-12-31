package day20

import readInput

class Number(val value: Int)

fun parseInput(input: List<String>): List<Number> {
    var parsed = mutableListOf<Number>()

    for (i in input) {
        parsed.add(Number(i.toInt()))
    }

    // There can be only one zero!
    check(parsed.count {it.value == 0} == 1)

    return parsed
}

fun findIndex(value: Number, mixedFile: List<Number>): Int = mixedFile.indexOfFirst {it == value}


fun findGroveCoordinate(mixedFile: List<Number>, offset: Int): Int {
    val pivot = findIndex(mixedFile.find {it.value == 0}!!, mixedFile)
    var index = (pivot + offset) % mixedFile.size

    return mixedFile[index].value
}

fun mix(number: Number, mixedFile: MutableList<Number>) {
    val oldIndex = findIndex(number, mixedFile)
    val unboundedNewIndex = oldIndex + number.value
    val newIndex = ((unboundedNewIndex % (mixedFile.size - 1)) + mixedFile.size - 1) % (mixedFile.size - 1)

    if (oldIndex == newIndex) return

    mixedFile.removeAt(oldIndex)
    mixedFile.add(newIndex, number)
}


fun main() {
    fun part1(input: List<String>): Int {
        val original = parseInput(input)
        var mixedFile = original.toMutableList()

        for (d in original) {
            mix(d, mixedFile)
        }

        val gc1 = findGroveCoordinate(mixedFile, 1000)
        val gc2 = findGroveCoordinate(mixedFile, 2000)
        val gc3 = findGroveCoordinate(mixedFile, 3000)

        println("Coordinates: $gc1 $gc2 $gc3")

        return gc1 + gc2 + gc3
    }

    fun part2(input: List<String>): Int = 0

    val testInput = readInput("Day20_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 0)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
