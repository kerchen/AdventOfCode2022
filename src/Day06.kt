import java.util.*
import kotlin.collections.ArrayDeque

fun main() {
    fun findUniqueWindow(input: String, windowSize: Int): Int {
        var window: ArrayDeque<Char> = ArrayDeque(windowSize)
        var index = 0
        for (c in input) {
            index += 1
            window.addFirst(c)
            if (window.size > windowSize) {
                window.removeLast()
                if (window.toSet().size == windowSize) {
                    return index
                }
            }
        }

        return index
    }

    fun part1(input: List<String>): List<Int> {
        var firstMarkers = mutableListOf<Int>()
        for (dataStream in input) {
            firstMarkers.add(findUniqueWindow(dataStream, 4))
        }
        println(firstMarkers)
        return firstMarkers
    }

    fun part2(input: List<String>): List<Int> {
        var firstMarkers = mutableListOf<Int>()
        for (dataStream in input) {
            firstMarkers.add(findUniqueWindow(dataStream, 14))
        }
        println(firstMarkers)
        return firstMarkers
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == listOf(7, 5, 6, 10, 11))
    check(part2(testInput) == listOf(19, 23, 23, 29, 26))

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
