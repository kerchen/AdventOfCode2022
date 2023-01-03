package day22

import readInput

enum class Facing(val weight: Int) {
    EAST(0) {
        override fun delta(): Point = Point(1, 0)
            },
    SOUTH(1) {
        override fun delta(): Point = Point(0, 1)
             },
    WEST(2) {
        override fun delta(): Point = Point(-1, 0)
            },
    NORTH(3) {
        override fun delta(): Point = Point(0, -1)
    };

    abstract fun delta(): Point
}

data class Point(val column: Int, val row: Int) {

}

class Cell(val passable: Boolean, val wallTravelDistances: IntArray) {

}

class Board(input: List<String>) {
    lateinit var startingPoint: Point
    val cells = mutableMapOf<Point, Cell>()
    var maxRow: Int
    var maxColumn: Int

    init {
        var row = 0
        var column = 1
        var foundStart = false

        maxRow = 0
        maxColumn = 0

        for (line in input) {
            var index = 0
            if (line.trim().isEmpty()) break

            row += 1
            maxRow = maxRow.coerceAtLeast(row)
            while (true) {
                val match = line.findAnyOf(listOf(".", "#"), index, true)

                if (match == null) break

                column = match.first + 1
                maxColumn = maxColumn.coerceAtLeast(column)
                index = column
                if (match.second == ".") {
                    if (!foundStart) {
                        foundStart = true
                        startingPoint = Point(column, row)
                    }
                    cells[Point(column, row)] = Cell(true, IntArray(4))
                } else {
                    cells[Point(column, row)] = Cell(false, IntArray(0))
                }
            }
        }

        fun step(point: Point, delta: Point): Point {
            var row = point.row + delta.row
            var column = point.column + delta.column

            if (delta.row < 0 && row < 0) row = maxRow
            if (delta.row > 0 && row > maxRow) row = 1
            if (delta.column < 0 && column < 0) column = maxColumn
            if (delta.column > 0 && column > maxColumn) column = 1

            return Point(column, row)
        }

        fun distanceToWall(startingPoint: Point, delta: Point): Int {
            var distance = 0
            var testPoint = step(startingPoint, delta)
            while (testPoint != startingPoint) {
                if (cells.containsKey(testPoint)) {
                    if (cells[testPoint]!!.passable) {
                        distance += 1
                    }
                    else break
                }
                testPoint = step(testPoint, delta)
            }
            return distance
        }

        // Compute distances to walls
        for (row in IntRange(1, maxRow)) {
            for (column in IntRange(1, maxColumn)) {
                val pt = Point(column, row)

                if (! cells.containsKey(pt) || ! cells[pt]!!.passable) continue

                for (d in Facing.values()) {
                    cells[pt]!!.wallTravelDistances[d.ordinal] = distanceToWall(pt, d.delta())
                }
            }
        }
    }
}
fun main() {
    fun part1(input: List<String>): Long {
        val board = Board(input)

        println("Board size is ${board.maxColumn} x ${board.maxRow}")
        return 0
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    val testInput = readInput("Day22_test")
    check(part1(testInput) == 6032.toLong())
    check(part2(testInput) == 0.toLong())

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}
