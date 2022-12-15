package day14

import readInput

data class Point(var x: Int, var y: Int)

enum class Occupier {
    ROCK,
    SAND
}

class Cave(rockPaths: List<String>) {
    var occupiedCells = HashMap<Point, Occupier>()
    var maxDepth = 0

    init {
        for (path in rockPaths) {
            val endPoints = path.split(Regex("->"))
            var lastPoint: Point? = null
            for (pt in endPoints) {
                val coords = pt.split(",")
                val point = Point(coords[0].trim().toInt(), coords[1].trim().toInt())
                if (lastPoint == null) {
                    lastPoint = point
                } else {
                    if (lastPoint.x == point.x) {
                        for (y in IntRange(minOf(lastPoint.y, point.y), maxOf(lastPoint.y, point.y)))
                            occupiedCells[Point(point.x, y)] = Occupier.ROCK
                    } else {
                        for (x in IntRange(minOf(lastPoint.x, point.x), maxOf(lastPoint.x, point.x)))
                            occupiedCells[Point(x, point.y)] = Occupier.ROCK
                    }
                    lastPoint = point
                }
                if (lastPoint.y > maxDepth) {
                    maxDepth = lastPoint.y
                }
            }
        }
    }

    enum class DIRECTION {
        DOWN,
        LEFT,
        RIGHT
    }

    fun dropSand(dropPoint: Point, floorOffset: Int = 0): Boolean {
        var direction = DIRECTION.DOWN
        var testPoint = dropPoint.copy(y=dropPoint.y+1)

        if (occupiedCells.containsKey(dropPoint)) {
            return false
        }

        while(testPoint.y <= maxDepth + floorOffset) {
            if (floorOffset > 0)
            {
                if (testPoint.y == maxDepth + floorOffset - 1){
                    for (x in IntRange(testPoint.x-1, testPoint.x+1))
                        occupiedCells[Point(x, testPoint.y + 1)] = Occupier.ROCK
                }
            }
            if (occupiedCells.containsKey(testPoint)) {
                when(direction) {
                    DIRECTION.DOWN -> {
                        direction = DIRECTION.LEFT
                        testPoint.x -= 1
                    }
                    DIRECTION.LEFT -> {
                        direction = DIRECTION.RIGHT
                        testPoint.x += 2
                    }
                    DIRECTION.RIGHT -> {
                        occupiedCells[Point(testPoint.x-1, testPoint.y-1)] = Occupier.SAND
                        return true
                    }
                }
            } else {
                testPoint.y += 1
                direction = DIRECTION.DOWN
            }
        }
        return false
    }
}
fun main() {
    fun part1(input: List<String>): Int {
        val cave = Cave(input)
        var sandUnits = 0
        while(cave.dropSand(Point(500, 0))) {
            sandUnits += 1
        }
        println(sandUnits)
        return sandUnits
    }

    fun part2(input: List<String>): Int {
        val cave = Cave(input)
        var sandUnits = 0
        while(cave.dropSand(Point(500, 0), 2)) {
            sandUnits += 1
        }
        println(sandUnits)
        return sandUnits
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
