package day18

import readInput
import java.lang.Math.abs

data class Point(val x: Int, val y: Int, val z: Int)

fun parseCubes(input: List<String>): List<Point> {
    var returnList = MutableList<Point>(0){ Point(0, 0, 0) }

    for (cube in input) {
        val components = cube.split(",")
        if (components.size == 3) {
            returnList.add(
                Point(
                    components[0].trim().toInt(),
                    components[1].trim().toInt(),
                    components[2].trim().toInt()
                )
            )
        }
    }
    return returnList
}

fun areAdjacent(p1: Point, p2: Point): Boolean {
    if (p1 == p2) return false
    val dx = abs(p1.x - p2.x)
    if (dx > 1) return false
    val dy = abs(p1.y - p2.y)
    if (dy > 1) return false
    val dz = abs(p1.z - p2.z)
    if (dz > 1) return false
    if (dx + dy + dz > 1) return false

    return true
}

class AdjacencyMatrix(cubes: List<Point>) {
    var neighborMap: MutableMap<Point, MutableList<Point>> = mutableMapOf()

    init {
        for (i in IntRange(0, cubes.size - 1)) {
            for (j in IntRange(i+1, cubes.size - 1)) {
                if (areAdjacent(cubes[i], cubes[j])) {
                    if (! neighborMap.containsKey(cubes[i])) {
                        neighborMap[cubes[i]] = mutableListOf()
                    }
                    neighborMap[cubes[i]]?.add(cubes[j])
                    if (! neighborMap.containsKey(cubes[j])) {
                        neighborMap[cubes[j]] = mutableListOf()
                    }
                    neighborMap[cubes[j]]?.add(cubes[i])
                }
            }
        }
    }
}

fun computeSurfaceArea(cubes: List<Point>): Int {
    var surfaceArea = 0
    val adjacencyMatrix = AdjacencyMatrix(cubes)

    for (c in cubes) {
        surfaceArea += 6 - (adjacencyMatrix.neighborMap[c]?.size ?: 0)
    }
    return surfaceArea
}

fun main() {
    fun part1(input: List<String>): Int  {
        val cubes = parseCubes(input)
        val surfaceArea = computeSurfaceArea(cubes)

        return surfaceArea
    }

    fun part2(input: List<String>): Int = 0

    val testInput = readInput("Day18_test")
    check(part1(testInput) == 64)
    check(part2(testInput) == 0)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
