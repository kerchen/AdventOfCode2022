package day17

import readInput

class GasSequence(input: String) {
    private var sequence: String = input.trim()
    private var sequenceIterator = sequence.iterator()

    fun next(): Char {
        if (!sequenceIterator.hasNext()) {
            //println("Gas iterator wrapping around")
            sequenceIterator = sequence.iterator()
        }
        return sequenceIterator.next()
    }
}


abstract class Shape {
    abstract fun getRows(): List<Array<Char>>
}

class HBar : Shape() {
    override fun getRows(): List<Array<Char>> {
        return listOf(arrayOf('@', '@', '@', '@'))
    }
}

class Plus : Shape() {
    override fun getRows(): List<Array<Char>> {
        return listOf(
            arrayOf('.', '@', '.'),
            arrayOf('@', '@', '@'),
            arrayOf('.', '@', '.')
        )
    }
}

class Hook : Shape() {
    override fun getRows(): List<Array<Char>> {
        return listOf(
            arrayOf('@', '@', '@'),
            arrayOf('.', '.', '@'),
            arrayOf('.', '.', '@')
        )
    }
}


class VBar : Shape() {
    override fun getRows(): List<Array<Char>> {
        return listOf(
            arrayOf('@'),
            arrayOf('@'),
            arrayOf('@'),
            arrayOf('@')
        )
    }
}


class Nugget : Shape() {
    override fun getRows(): List<Array<Char>> {
        return listOf(
            arrayOf('@', '@'),
            arrayOf('@', '@'),
        )
    }
}


fun padRow(row: Array<Char>, offset: Int, rowWidth: Int): Array<Char> {
    return Array(offset) {' '} + row.copyOf() + Array(rowWidth - offset - row.size) {' '}
}


fun isPartMoving(part: Char): Boolean = part == '.' || part == '@'

class Chamber(val width: Int = 7) {
    var rows = mutableListOf<Array<Char>>()
    var topOccupiedRow = 0

    init {
        rows.add(Array(width) {'#'})
    }

    fun dropShape(shape: Shape, gas: GasSequence) {
        while(rows.size < topOccupiedRow + 4) {
            rows.add(Array(width) {' '})
        }
        var bottom = rows.size
        val shapeRows = shape.getRows()
        for (row in shapeRows) {
            rows.add(padRow(row, 2, width))
        }
        var maxNewTopOccupiedRow = topOccupiedRow + shapeRows.size
        var lastTopOccupiedRow = topOccupiedRow
        var keepGoing = true
        while (keepGoing) {
            val lateral = gas.next()
            when (lateral) {
                '>' -> {
                    //println(">")
                    //dump()
                    if (canMoveRight(bottom)) {
                        moveRight(bottom)
                        println("moved >")
                        dump()
                    } else println("could not move >")
                }
                '<' -> {
                    //println("<")
                    //dump()
                    if (canMoveLeft(bottom)) {
                        moveLeft(bottom)
                        println("moved <")
                        dump()
                    } else println("could not move <")
                }
                else -> throw Exception("Unexpected gas direction")
            }
            //println("V")
            if (canMoveDown(bottom)) {
                moveDown(bottom)
                println("moved V")
                dump()
                bottom -= 1
            } else {
                keepGoing = false
                solidify(bottom)
                println("solidified")
                println("")
                dump()
            }
        }
        check(maxNewTopOccupiedRow >= topOccupiedRow)
        check(lastTopOccupiedRow <= topOccupiedRow)
    }

    fun solidify(bottom: Int) {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (rows[row][col] == '@') rows[row][col] = '#'
                else if (rows[row][col] == '.') rows[row][col] = ' '
            }
        }
        var emptyCount = rows.last().count {it == ' '}
        while (emptyCount == width){
            rows.removeLast()
            emptyCount = rows.last().count {it == ' '}
        }
        topOccupiedRow = rows.size - 1
    }

    fun canMoveDown(bottom: Int): Boolean {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (rows[row][col] == '@') {
                    val neighbor = rows[row-1][col]
                    if (neighbor != ' ' && ! isPartMoving(neighbor))
                        return false
                }
            }
        }
        return true
    }

    fun canMoveRight(bottom: Int): Boolean {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (isPartMoving(rows[row][col])) {
                    if (col == width - 1)
                        return false
                    val neighbor = rows[row][col+1]
                    if (neighbor != ' ' && ! isPartMoving(neighbor))
                        return false
                }
            }
        }
        return true
    }

    fun canMoveLeft(bottom: Int): Boolean {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (isPartMoving(rows[row][col])) {
                    if (col == 0)
                        return false
                    val neighbor = rows[row][col-1]
                    if (neighbor != ' ' && ! isPartMoving(neighbor))
                        return false
                }
            }
        }
        return true
    }

    fun moveDown(bottom: Int) {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (isPartMoving(rows[row][col])) {
                    if (rows[row][col] == '@' || rows[row-1][col] == ' ') {
                        rows[row - 1][col] = rows[row][col]
                        rows[row][col] = ' '
                    }
                    //if (row < rows.size-1) {
                        //rows[row][col] = rows[row+1][col]
                    //} else {
                        //rows[row][col] = ' '
                    //}
                } //else {
                   // rows[row][col] = ' '
                //}
            }
        }
    }

    fun moveRight(bottom: Int) {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (isPartMoving(rows[row][width - col - 1])) {
                    rows[row][width - col] = rows[row][width - col - 1]
                    if (col < width - 1) {
                        //if (!isPartMoving(rows[row][width - col - 2]))
                            //rows[row][width - col - 1] = ' '
                            rows[row][width - col - 1] = rows[row][width - col - 2]
                        //else
                            //rows[row][width - col - 1] = ' '
                    } else {
                        rows[row][width - col - 1] = ' '
                    }
                }
            }
        }
    }

    fun moveLeft(bottom: Int) {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (isPartMoving(rows[row][col])) {
                    rows[row][col-1] = rows[row][col]
                    if (col < width - 1) {
                        if (!isPartMoving(rows[row][col + 1]))
                            rows[row][col] = ' '
                            //rows[row][col] = rows[row][col + 1]
                        //else
                            //rows[row][col] = ' '
                    } else {
                        rows[row][col] = ' '
                    }
                }
            }
        }
    }

    fun dump() {
        for (row in IntRange(0, rows.size - 1))
        {
            print("%4d |".format(rows.size-row-1))
            for (i in IntRange(0, width-1))
                print(rows[rows.size - row - 1][i])
            println("|")
        }
        println("")
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val gasSequence = GasSequence(input[0])
        val chamber = Chamber()

        //for (i in IntRange(0, 2021)) {
        for (i in IntRange(0, 20)) {
            println("Dropping shape $i:")
            when (i % 5) {
                0 -> chamber.dropShape(HBar(), gasSequence)
                1 -> chamber.dropShape(Plus(), gasSequence)
                2 -> chamber.dropShape(Hook(), gasSequence)
                3 -> chamber.dropShape(VBar(), gasSequence)
                4 -> chamber.dropShape(Nugget(), gasSequence)
            }
            chamber.dump()
        }
        println(chamber.topOccupiedRow)
        return chamber.topOccupiedRow
    }

    fun part2(input: List<String>): Int = 0

    val testInput = readInput("Day17_test")
    check(part1(testInput) == 3068)
    check(part2(testInput) == 0)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
