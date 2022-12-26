package day17

import readInput

class GasSequence(input: String) {
    private var sequence: String = input.trim()
    private var sequenceIterator = sequence.iterator()

    fun next(): Char {
        if (!sequenceIterator.hasNext()) {
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
            arrayOf(' ', '@', ' '),
            arrayOf('@', '@', '@'),
            arrayOf(' ', '@', ' ')
        )
    }
}

class Hook : Shape() {
    override fun getRows(): List<Array<Char>> {
        return listOf(
            arrayOf('@', '@', '@'),
            arrayOf(' ', ' ', '@'),
            arrayOf(' ', ' ', '@')
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

fun autocorrelate(signal: List<Int>, initialOffset: Int, minSequenceLength: Int): Pair<Int, Int> {
    var x = initialOffset
    var offset = 0
    var repeatStartX = signal.size
    var matchLength = 0
    var repeatFound = false
    while (! repeatFound && x < signal.size - minSequenceLength) {
        x = initialOffset + offset
        var testX = x + minSequenceLength
        while (testX < signal.size) {
            if (signal[x] == signal[testX]) {
                repeatStartX = testX
                matchLength = 1
                x += 1
                testX += 1
                while (x < repeatStartX && testX < signal.size && signal[x] == signal[testX]) {
                    matchLength += 1
                    x += 1
                    testX += 1
                }
                if (x == repeatStartX) {
                    repeatFound = true
                    break
                }
            } else {
                testX += 1
            }
        }
        offset += 1
    }
    return Pair(repeatStartX, matchLength)
}

fun Array<Char>.toInt(): Int {
    var value = 0
    for (c in this) {
        value *= 2
        if (c != ' ') {
            value += 1
        }
    }
    return value
}

class Chamber(val width: Int = 7) {
    var rows = mutableListOf<Array<Char>>()
    var topOccupiedRow = 0
    var shapeBottomRow = 0

    init {
        rows.add(Array(width) {'#'})
    }

    fun toList(): List<Int> {
        var returnList: MutableList<Int> = MutableList(rows.size) {0}

        for (row in IntRange(1, rows.size-1)) {
            returnList[row] = rows[row].toInt()
        }
        return returnList
    }
    fun addStartingGap() {
        while(rows.size < topOccupiedRow + 4) {
            rows.add(Array(width) {' '})
        }
    }
    fun addShape(shape: Shape, offset: Int) {
        val shapeRows = shape.getRows()
        shapeBottomRow = rows.size
        for (row in shapeRows) {
            rows.add(padRow(row, offset, width))
        }
    }
    fun dropShape(shape: Shape, gas: GasSequence) {
        addStartingGap()
        addShape(shape, 2)
        var keepGoing = true
        while (keepGoing) {
            val lateral = gas.next()
            when (lateral) {
                '>' -> {
                    if (canMoveRight(shapeBottomRow)) {
                        moveRight(shapeBottomRow)
                    }
                }
                '<' -> {
                    if (canMoveLeft(shapeBottomRow)) {
                        moveLeft(shapeBottomRow)
                    }
                }
                else -> throw Exception("Unexpected gas direction")
            }
            if (canMoveDown(shapeBottomRow)) {
                shapeBottomRow = moveDown(shapeBottomRow)
            } else {
                keepGoing = false
                solidify(shapeBottomRow)
            }
        }
    }

    fun solidify(bottom: Int) {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (rows[row][col] == '@') rows[row][col] = '#'
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
                    if (neighbor != ' ' && neighbor != '@')
                        return false
                }
            }
        }
        return true
    }

    fun canMoveRight(bottom: Int): Boolean {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (rows[row][col] == '@') {
                    if (col == width - 1)
                        return false
                    val neighbor = rows[row][col+1]
                    if (neighbor != ' ' && neighbor != '@')
                        return false
                }
            }
        }
        return true
    }

    fun canMoveLeft(bottom: Int): Boolean {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (rows[row][col] == '@') {
                    if (col == 0)
                        return false
                    val neighbor = rows[row][col-1]
                    if (neighbor != ' ' && neighbor != '@')
                        return false
                }
            }
        }
        return true
    }

    fun moveDown(bottom: Int): Int {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (rows[row][col] == '@') {
                    if (rows[row][col] == '@' || rows[row-1][col] == ' ') {
                        rows[row - 1][col] = rows[row][col]
                        rows[row][col] = ' '
                    }
                }
            }
        }
        return bottom - 1
    }

    fun moveRight(bottom: Int) {
        for (row in IntRange(bottom, rows.size - 1)) {
            for (col in IntRange(0, width-1)) {
                if (rows[row][width - col - 1] == '@') {
                    rows[row][width - col] = rows[row][width - col - 1]
                    if (col < width - 1 && rows[row][width - col - 2] == '@') {
                        rows[row][width - col - 1] = rows[row][width - col - 2]
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
                if (rows[row][col] == '@') {
                    rows[row][col-1] = rows[row][col]
                    if (col < width - 1) {
                        if (rows[row][col + 1] != '@')
                            rows[row][col] = ' '
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
            //print("|")
            for (i in IntRange(0, width-1)) {
                val c = rows[rows.size - row -1][i]
                if (c == ' ')
                    print('.')
                else
                    print(c)
            }
            println("| ${rows[rows.size - row - 1].toInt()}")
        }
        println("")
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val gasSequence = GasSequence(input[0])
        val chamber = Chamber()

        for (i in IntRange(0, 2021)) {
            when (i % 5) {
                0 -> chamber.dropShape(HBar(), gasSequence)
                1 -> chamber.dropShape(Plus(), gasSequence)
                2 -> chamber.dropShape(Hook(), gasSequence)
                3 -> chamber.dropShape(VBar(), gasSequence)
                4 -> chamber.dropShape(Nugget(), gasSequence)
            }
        }
        return chamber.topOccupiedRow
    }

    fun part2(input: List<String>): Long {
        val gasSequence = GasSequence(input[0])
        val chamber = Chamber()
        var lastRow = 0
        var deltas: MutableList<Int> = MutableList(0) {0}

        // drop enough shapes to determine the repeating pattern of height changes that
        // emerges after the initial non-repeating sequence.
        for (i in IntRange(0, 100000-1)) {
            when (i % 5) {
                0 -> chamber.dropShape(HBar(), gasSequence)
                1 -> chamber.dropShape(Plus(), gasSequence)
                2 -> chamber.dropShape(Hook(), gasSequence)
                3 -> chamber.dropShape(VBar(), gasSequence)
                4 -> chamber.dropShape(Nugget(), gasSequence)
            }
            deltas.add(chamber.topOccupiedRow - lastRow)
            lastRow = chamber.topOccupiedRow
        }
        check(deltas.size == 100000)
        val repeatDelta = autocorrelate(deltas, 1, 10)
        val piecesPerRepeat = repeatDelta.second
        val initialPieces = repeatDelta.first
        val initialHeight = deltas.slice(0..initialPieces-1).sum()
        val repeatingHeightChange: Long = deltas.slice(initialPieces..initialPieces+piecesPerRepeat-1).sum().toLong()
        println("Initial height: $initialHeight (made from $initialPieces pieces)")
        println("Every $piecesPerRepeat pieces results in a height change of $repeatingHeightChange")
        val totalPieceCount: Long = 1000000000000
        val repeatedPieceGroups: Long = (totalPieceCount - initialPieces) / piecesPerRepeat
        val remainingPieces: Long = totalPieceCount - initialPieces - repeatedPieceGroups * piecesPerRepeat
        val remainingHeight = deltas.slice(initialPieces .. initialPieces + remainingPieces.toInt() - 1).sum()
        val towerHeight: Long = initialHeight.toLong() + repeatedPieceGroups * repeatingHeightChange + remainingHeight.toLong()
        return towerHeight
    }

    val testInput = readInput("Day17_test")
    check(part1(testInput) == 3068)
    check(part2(testInput) == 1514285714288)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
