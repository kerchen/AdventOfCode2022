package day08

import readInput

class HeightMap(input: List<String>) {
    var rows = mutableListOf<List<Int>>()
    var rowCount = 0
    var columnCount = 0

    init {
        for (row in input) {
            rows.add(row.map { it.code - '0'.code })
        }
        rowCount = rows.size
        columnCount = rows[0].size
    }

    fun countVisible(): Int {
        var visibleTrees = rowCount * 2 + columnCount * 2 - 4 // Edge trees are visible automatically
        for (row in IntRange(1, rowCount - 2)) {
            for (column in IntRange(1, columnCount - 2)) {
                val height = rows[row][column]

                if (height == 0 )
                    continue

                var blocked = false
                // Look West
                for (i in IntRange(0, column - 1)) {
                    if (rows[row][i] >= height) {
                        blocked = true
                        break
                    }
                }
                // Look East
                if (blocked) {
                    blocked = false
                    for (i in IntRange(column + 1, columnCount - 1)) {
                        if (rows[row][i] >= height) {
                            blocked = true
                            break
                        }
                    }
                }
                // Look North
                if (blocked) {
                    blocked = false
                    for (j in IntRange(0, row - 1)) {
                        if (rows[j][column] >= height) {
                            blocked = true
                            break
                        }
                    }
                }
                // Look South
                if (blocked) {
                    blocked = false
                    for (j in IntRange(row + 1, rowCount - 1)) {
                        if (rows[j][column] >= height) {
                            blocked = true
                            break
                        }
                    }
                }

                if (! blocked) {
                    visibleTrees += 1
                }
            }
        }

        return visibleTrees
    }

    fun computeBestScenicScore(): Int {
        var bestScore = 0

        for (row in IntRange(1, rowCount - 2)) {
            for (column in IntRange(1, columnCount - 2)) {
                val height = rows[row][column]

                if (height == 0 )
                    continue

                var score = 0
                var rangeEnd = 0
                // Look West
                rangeEnd = column
                for (i in IntRange(1, rangeEnd)) {
                    if (rows[row][column-i] >= height || i == rangeEnd) {
                        score = i
                        break
                    }
                }
                // Look East
                rangeEnd = columnCount - column - 1
                for (i in IntRange(1, rangeEnd)) {
                    if (rows[row][column+i] >= height || i == rangeEnd) {
                        score *= i
                        break
                    }
                }
                // Look North
                rangeEnd = row
                for (j in IntRange(1, rangeEnd)) {
                    if (rows[row-j][column] >= height || j == rangeEnd) {
                        score *= j
                        break
                    }
                }
                // Look South
                rangeEnd = rowCount - row - 1
                for (j in IntRange(1, rangeEnd)) {
                    if (rows[row+j][column] >= height || j == rangeEnd) {
                        score *= j
                        break
                    }
                }
                if (score > bestScore) {
                    bestScore = score
                }
            }
        }
        return bestScore
    }
}

fun main() {
    fun part1(input: List<String>): Int = HeightMap(input).countVisible()

    fun part2(input: List<String>): Int = HeightMap(input).computeBestScenicScore()

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
