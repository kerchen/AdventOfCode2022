package day22

import org.testng.annotations.Test

class Day22KtTest {
    @Test
    fun `move east one step from starting point`() {
        val board = Board(listOf("  ...",
                                 "  ..."))
        val endPoint = board.move(board.startingPoint, Facing.EAST, 1)
        assert(endPoint == Point(board.startingPoint.column + 1, board.startingPoint.row))
    }

    @Test
    fun `move east several steps from starting point`() {
        val board = Board(listOf("  ......   ",
                                 "  ......   "))
        val steps = 4
        val endPoint = board.move(board.startingPoint, Facing.EAST, steps)
        assert(endPoint == Point(board.startingPoint.column + steps, board.startingPoint.row))
    }

    @Test
    fun `move east enough steps from starting point to return to starting point`() {
        val board = Board(listOf("  ......   ",
                                 "  ......   "))
        val steps = 6
        val endPoint = board.move(board.startingPoint, Facing.EAST, steps)
        assert(endPoint == board.startingPoint)
    }

    @Test
    fun `move east from starting point to wall`() {
        val board = Board(listOf("  ...#..   ",
                                 "  ......   "))
        val steps = 2
        val endPoint = board.move(board.startingPoint, Facing.EAST, steps)
        assert(endPoint == board.startingPoint.copy(column = board.startingPoint.column + 2))
    }

    @Test
    fun `move east from starting point stops short at wall`() {
        val board = Board(listOf("  ...#..   ",
                                 "  ......   "))
        val steps = 6
        val endPoint = board.move(board.startingPoint, Facing.EAST, steps)
        assert(endPoint == board.startingPoint.copy(column = board.startingPoint.column + 2))
    }

    @Test
    fun `move west from starting point stops short at wall`() {
        val board = Board(listOf("  ...#..   ",
                                 "  ......   "))
        val steps = 16
        val endPoint = board.move(board.startingPoint, Facing.WEST, steps)
        assert(endPoint == board.startingPoint.copy(column = 7))
    }

    @Test
    fun `move north from starting point stops short at wall`() {
        val board = Board(listOf("  ...#..   ",
                                 "  #.....   ",
                                 "  ......   ",
                                 "   .....   "
        ))
        val steps = 16
        val endPoint = board.move(board.startingPoint, Facing.NORTH, steps)
        assert(endPoint == board.startingPoint.copy(row = 3))
    }
}