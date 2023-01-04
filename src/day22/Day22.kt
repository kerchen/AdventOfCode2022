package day22

import readInput
import java.util.regex.Pattern

enum class Facing(val weight: Int) {
    EAST(0) {
        override fun delta(): Point = Point(1, 0)
        override fun left(): Facing = NORTH
        override fun right(): Facing = SOUTH
            },
    SOUTH(1) {
        override fun delta(): Point = Point(0, 1)
        override fun left(): Facing = EAST
        override fun right(): Facing = WEST
             },
    WEST(2) {
        override fun left(): Facing = SOUTH
        override fun right(): Facing = NORTH
        override fun delta(): Point = Point(-1, 0)
            },
    NORTH(3) {
        override fun delta(): Point = Point(0, -1)
        override fun left(): Facing = WEST
        override fun right(): Facing = EAST
    };

    abstract fun delta(): Point
    abstract fun left(): Facing
    abstract fun right(): Facing
}

data class Point(val column: Int, val row: Int)

class Cell(val passable: Boolean, val wallTravelDistances: IntArray) {

}

class Board(input: List<String>) {
    lateinit var startingPoint: Point
    val cells = mutableMapOf<Point, Cell>()
    val validColumnsForRow = mutableMapOf<Int, Pair<Int, Int>>()
    val validRowsForColumn = mutableMapOf<Int, Pair<Int, Int>>()
    var maxRow: Int
    var maxColumn: Int

    init {
        var row = 0
        var foundStart = false

        maxRow = 0
        maxColumn = 0

        for (line in input) {
            var index = 0
            var firstColumn = 0
            var column: Int
            if (line.trim().isEmpty()) break

            row += 1
            maxRow = maxRow.coerceAtLeast(row)
            while (true) {
                val match = line.findAnyOf(listOf(".", "#"), index, true)

                if (match == null) break

                column = match.first + 1
                if (firstColumn == 0) {
                    firstColumn = column
                    validColumnsForRow[row] = Pair(column, 0)
                } else {
                    validColumnsForRow[row] = Pair(firstColumn, column)
                }
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

        for (column in IntRange(1, maxColumn)) {
            var firstRow = 0
            for (row in IntRange(1, maxRow)) {
                if (cells.containsKey(Point(column, row))) {
                    if (firstRow == 0) {
                        firstRow = row
                        validRowsForColumn[column] = Pair(firstRow, 0)
                    } else {
                        validRowsForColumn[column] = Pair(firstRow, row)
                    }
                }
            }
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
            return if (testPoint != startingPoint) distance else Int.MAX_VALUE
        }

        // Compute distances to walls
        for (cell in cells) {
            if (cell.value.passable) {
                for (d in Facing.values()) {
                    cell.value.wallTravelDistances[d.ordinal] = distanceToWall(cell.key, d.delta())
                }
            }
        }
    }

    fun step(point: Point, delta: Point, steps: Int = 1): Point {
        var row = point.row
        var column = point.column

        // Assume no diagonal movement. I may regret this assumption in part 2.
        check(delta.row == 0 || delta.column == 0)

        for (i in IntRange(1, steps)) {
            row += delta.row
            column += delta.column

            if (delta.row < 0 && row < validRowsForColumn[column]!!.first) row = validRowsForColumn[column]!!.second
            if (delta.row > 0 && row > validRowsForColumn[column]!!.second) row = validRowsForColumn[column]!!.first
            if (delta.column < 0 && column < validColumnsForRow[row]!!.first) column = validColumnsForRow[row]!!.second
            if (delta.column > 0 && column > validColumnsForRow[row]!!.second) column = validColumnsForRow[row]!!.first
        }
        return Point(column, row)
    }

    fun move(startingLocation: Point, facing: Facing, steps: Int): Point {
        if (cells.containsKey(startingLocation)) {
            if (cells[startingLocation]!!.passable) {
                val stepCount = steps.coerceAtMost(cells[startingLocation]!!.wallTravelDistances[facing.ordinal])
                return step(startingLocation, facing.delta(), stepCount)
            }
        }
        return startingLocation
    }
}

class Pawn(var location: Point) {
    var facing = Facing.EAST
}

fun main() {
    fun part1(input: List<String>): Long {
        val board = Board(input)
        val directions = input.lastOrNull() { it.contains(Regex("[0-9]+")) }
        var index = 0
        val pattern = Pattern.compile( """([0-9]+)([RL]?)""")
        val pawn = Pawn(board.startingPoint)
        println("Board size is ${board.maxColumn} x ${board.maxRow}")
        println("Directions: $directions")
        while (true) {
            val matcher = pattern.matcher(directions!!.substring(index))
            if (matcher.find()) {
                val steps = matcher.group(1).toInt()
                println("Move $steps ${pawn.facing}")
                pawn.location = board.move(pawn.location, pawn.facing, steps)
                println("After steps: ${pawn.location.column}  ${pawn.location.row}  ${pawn.facing}")
                if (matcher.group(2).length > 0) {
                    val newFacing = when (matcher.group(2)[0]) {
                        'R' -> pawn.facing.right()
                        'L' -> pawn.facing.left()
                        else -> throw Exception("Unexpected direction")
                    }
                    pawn.facing = newFacing
                }
                index += matcher.group(0).length
            } else {
                break
            }
        }
        println("Final pawn position & facing: ${pawn.location.row}  ${pawn.location.column}  ${pawn.facing.ordinal}")
        return (1000 * pawn.location.row + 4 * pawn.location.column + pawn.facing.ordinal).toLong()
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
