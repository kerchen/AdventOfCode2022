import java.lang.Math.abs

data class Position(var x: Int, var y: Int) {
    fun move(deltaX: Int, deltaY: Int) {
        x += deltaX
        y += deltaY
    }

    fun follow(position: Position): Boolean {
        var moved = false
        val dx = position.x - x
        val dy = position.y - y
        val diagonal = (abs(dx) + abs(dy) > 2)

        /*
        if (diagonal) {
            print("Diagonal ")
        }
        */
        if (abs(dx) > 1 || diagonal) {
            x += abs(dx) / dx
            moved = true
        }
        if (abs(dy) > 1 || diagonal) {
            y += abs(dy) / dy
            moved = true
        }
        return moved
    }
}

data class Move(val move: String) {
    var deltaX = 0
    var deltaY = 0
    var steps = 0

    init {
        val parsedMove = move.split(" ")

        steps = parsedMove[1].toInt()

        when(parsedMove[0]) {
            "R" -> deltaX = 1
            "L" -> deltaX = -1
            "U" -> deltaY = -1
            "D" -> deltaY = 1
            else -> throw Exception("Unexpected move direction")
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        var headPosition = Position(0, 0)
        var tailPosition = Position(0, 0)
        var tailVisits = mutableSetOf(tailPosition.copy())

        for (moveString in input) {
            val move = Move(moveString)
            //println("Move: ${moveString}")
            var stepsRemaining = move.steps
            while(stepsRemaining > 0) {
                headPosition.move(move.deltaX, move.deltaY)
                //println("Head moved to ${headPosition.x}, ${headPosition.y}")
                if (tailPosition.follow(headPosition)) {
                    //println("Tail moved to ${tailPosition.x}, ${tailPosition.y}")
                    tailVisits.add(tailPosition.copy())
                }

                stepsRemaining -= 1
            }
        }
        return tailVisits.size
    }

    fun part2(input: List<String>): Int {
        val knotCount = 10
        var knotPositions = MutableList(knotCount) {Position(0, 0)}
        var tailVisits = mutableSetOf(knotPositions[knotCount-1].copy())

        for (moveString in input) {
            val move = Move(moveString)
            //println("Move: ${moveString}")
            var stepsRemaining = move.steps
            while(stepsRemaining > 0) {
                knotPositions[0].move(move.deltaX, move.deltaY)
                //println("Head moved to ${knotPositions[0].x}, ${knotPositions[0].y}")
                for (i in IntRange(1, knotCount-1)) {
                    knotPositions[i].follow(knotPositions[i - 1])
                }
                tailVisits.add(knotPositions[knotCount-1].copy())
                stepsRemaining -= 1
            }
        }
        return tailVisits.size
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)

    val test2Input = readInput("Day09_test2")
    check(part1(test2Input) == 8)

    val testPart2Input = readInput("Day09_part2_test")
    check(part2(testPart2Input) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
