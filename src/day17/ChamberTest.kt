package day17

import org.testng.annotations.BeforeTest
import org.testng.annotations.Test


class ChamberTest {
    //lateinit var chamber: Chamber

    //@BeforeTest
    //fun setup() {
    //chamber = Chamber()
    //}

    @Test
    fun `hbar initial placement can move down`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(HBar(), 0)

        assert(chamber.canMoveDown(chamber.shapeBottomRow))
    }

    @Test
    fun `hbar move down`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(HBar(), 0)

        chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.rows[3].contentEquals(arrayOf('@', '@', '@', '@', ' ', ' ', ' ')))
    }

    @Test
    fun `hbar move right from left edge`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(HBar(), 0)

        chamber.moveRight(chamber.shapeBottomRow)
        assert(chamber.rows[4].contentEquals(arrayOf(' ', '@', '@', '@', '@', ' ', ' ')))
    }

    @Test
    fun `hbar move left from right edge`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(HBar(), 3)

        chamber.moveLeft(chamber.shapeBottomRow)
        assert(chamber.rows[4].contentEquals(arrayOf(' ', ' ', '@', '@', '@', '@', ' ')))
    }

    @Test
    fun `plus move down`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(Plus(), 0)

        chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.rows[5].contentEquals(arrayOf(' ', '@', ' ', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[4].contentEquals(arrayOf('@', '@', '@', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[3].contentEquals(arrayOf(' ', '@', ' ', ' ', ' ', ' ', ' ')))
    }

    @Test
    fun `plus move right and down 3 times`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(Plus(), 0)

        chamber.moveRight(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.moveRight(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.moveRight(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.rows[3].contentEquals(arrayOf(' ', ' ', ' ', ' ', '@', ' ', ' ')))
        assert(chamber.rows[2].contentEquals(arrayOf(' ', ' ', ' ', '@', '@', '@', ' ')))
        assert(chamber.rows[1].contentEquals(arrayOf(' ', ' ', ' ', ' ', '@', ' ', ' ')))
    }

    @Test
    fun `plus move left and down 3 times`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(Plus(), 4)

        chamber.moveLeft(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.moveLeft(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.moveLeft(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.rows[3].contentEquals(arrayOf(' ', ' ', '@', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[2].contentEquals(arrayOf(' ', '@', '@', '@', ' ', ' ', ' ')))
        assert(chamber.rows[1].contentEquals(arrayOf(' ', ' ', '@', ' ', ' ', ' ', ' ')))
    }

    @Test
    fun `plus move right from left edge`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(Plus(), 0)

        chamber.moveRight(chamber.shapeBottomRow)
        assert(chamber.rows[6].contentEquals(arrayOf(' ', ' ', '@', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[5].contentEquals(arrayOf(' ', '@', '@', '@', ' ', ' ', ' ')))
        assert(chamber.rows[4].contentEquals(arrayOf(' ', ' ', '@', ' ', ' ', ' ', ' ')))
    }

    @Test
    fun `plus move left from right edge`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(Plus(), 4)

        chamber.moveLeft(chamber.shapeBottomRow)
        assert(chamber.rows[6].contentEquals(arrayOf(' ', ' ', ' ', ' ', '@', ' ', ' ')))
        assert(chamber.rows[5].contentEquals(arrayOf(' ', ' ', ' ', '@', '@', '@', ' ')))
        assert(chamber.rows[4].contentEquals(arrayOf(' ', ' ', ' ', ' ', '@', ' ', ' ')))
    }

    @Test
    fun `hook move down`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(Hook(), 0)

        chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.rows[5].contentEquals(arrayOf(' ', ' ', '@', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[4].contentEquals(arrayOf(' ', ' ', '@', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[3].contentEquals(arrayOf('@', '@', '@', ' ', ' ', ' ', ' ')))
    }


    @Test
    fun `hook move right and down 3 times`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(Hook(), 0)

        chamber.moveRight(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.moveRight(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.moveRight(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.rows[3].contentEquals(arrayOf(' ', ' ', ' ', ' ', ' ', '@', ' ')))
        assert(chamber.rows[2].contentEquals(arrayOf(' ', ' ', ' ', ' ', ' ', '@', ' ')))
        assert(chamber.rows[1].contentEquals(arrayOf(' ', ' ', ' ', '@', '@', '@', ' ')))
    }

    @Test
    fun `hook move left and down 3 times`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(Hook(), 4)

        chamber.moveLeft(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.moveLeft(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.moveLeft(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.rows[3].contentEquals(arrayOf(' ', ' ', ' ', '@', ' ', ' ', ' ')))
        assert(chamber.rows[2].contentEquals(arrayOf(' ', ' ', ' ', '@', ' ', ' ', ' ')))
        assert(chamber.rows[1].contentEquals(arrayOf(' ', '@', '@', '@', ' ', ' ', ' ')))
    }

    @Test
    fun `vbar move down`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(VBar(), 0)

        chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.rows[6].contentEquals(arrayOf('@', ' ', ' ', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[5].contentEquals(arrayOf('@', ' ', ' ', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[4].contentEquals(arrayOf('@', ' ', ' ', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[3].contentEquals(arrayOf('@', ' ', ' ', ' ', ' ', ' ', ' ')))
    }

    @Test
    fun `vbar full sweep left to right`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(VBar(), 0)

        for (i in IntRange(1, 6))
            chamber.moveRight(chamber.shapeBottomRow)
        assert(chamber.rows[7].contentEquals(arrayOf(' ', ' ', ' ', ' ', ' ', ' ', '@')))
        assert(chamber.rows[6].contentEquals(arrayOf(' ', ' ', ' ', ' ', ' ', ' ', '@')))
        assert(chamber.rows[5].contentEquals(arrayOf(' ', ' ', ' ', ' ', ' ', ' ', '@')))
        assert(chamber.rows[4].contentEquals(arrayOf(' ', ' ', ' ', ' ', ' ', ' ', '@')))
    }

    @Test
    fun `vbar full sweep right to left`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(VBar(), 6)

        for (i in IntRange(1, 6))
            chamber.moveLeft(chamber.shapeBottomRow)
        assert(chamber.rows[7].contentEquals(arrayOf('@', ' ', ' ', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[6].contentEquals(arrayOf('@', ' ', ' ', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[5].contentEquals(arrayOf('@', ' ', ' ', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[4].contentEquals(arrayOf('@', ' ', ' ', ' ', ' ', ' ', ' ')))
    }

    @Test
    fun `nugget move down`() {
        var chamber = Chamber()
        chamber.addStartingGap()
        chamber.addShape(Nugget(), 0)

        chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.rows[4].contentEquals(arrayOf('@', '@', ' ', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[3].contentEquals(arrayOf('@', '@', ' ', ' ', ' ', ' ', ' ')))
    }

    @Test
    fun `plus can slip into notch from right`() {
        var chamber = Chamber()
        chamber.rows.addAll(
            listOf(
                arrayOf('#', '#', '#', ' ', ' ', ' ', ' '),
                arrayOf('#', '#', ' ', ' ', ' ', ' ', ' '),
                arrayOf('#', '#', '#', ' ', ' ', ' ', ' ')
            )
        )
        chamber.addShape(Plus(), 3)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.canMoveLeft(chamber.shapeBottomRow))
        chamber.moveLeft(chamber.shapeBottomRow)
        assert(chamber.rows[3].contentEquals(arrayOf('#', '#', '#', '@', ' ', ' ', ' ')))
        assert(chamber.rows[2].contentEquals(arrayOf('#', '#', '@', '@', '@', ' ', ' ')))
        assert(chamber.rows[1].contentEquals(arrayOf('#', '#', '#', '@', ' ', ' ', ' ')))
    }

    @Test
    fun `plus can slip into notch from left`() {
        var chamber = Chamber()
        chamber.rows.addAll(
            listOf(
                arrayOf(' ', ' ', ' ', ' ', '#', '#', '#'),
                arrayOf(' ', ' ', ' ', ' ', ' ', '#', '#'),
                arrayOf(' ', ' ', ' ', ' ', '#', '#', '#')
            )
        )
        chamber.addShape(Plus(), 1)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.canMoveRight(chamber.shapeBottomRow))
        chamber.moveRight(chamber.shapeBottomRow)
        assert(chamber.rows[3].contentEquals(arrayOf(' ', ' ', ' ', '@', '#', '#', '#')))
        assert(chamber.rows[2].contentEquals(arrayOf(' ', ' ', '@', '@', '@', '#', '#')))
        assert(chamber.rows[1].contentEquals(arrayOf(' ', ' ', ' ', '@', '#', '#', '#')))
    }

    @Test
    fun `hook can slip into notch from right`() {
        var chamber = Chamber()
        chamber.rows.addAll(
            listOf(
                arrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' '),
                arrayOf('#', '#', ' ', ' ', ' ', ' ', ' '),
                arrayOf('#', '#', ' ', ' ', ' ', ' ', ' ')
            )
        )
        chamber.addShape(Hook(), 2)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        assert(chamber.canMoveLeft(chamber.shapeBottomRow))
        chamber.moveLeft(chamber.shapeBottomRow)
        assert(chamber.canMoveLeft(chamber.shapeBottomRow))
        chamber.moveLeft(chamber.shapeBottomRow)
        assert(chamber.rows[3].contentEquals(arrayOf('#', '#', '@', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[2].contentEquals(arrayOf('#', '#', '@', ' ', ' ', ' ', ' ')))
        assert(chamber.rows[1].contentEquals(arrayOf('@', '@', '@', ' ', ' ', ' ', ' ')))

    }

    @Test
    fun `hook can follow stairs down and right`() {
        var chamber = Chamber()
        chamber.rows.addAll(
            listOf(
                arrayOf(' ', '#', '#', '#', '#', '#', ' '),
                arrayOf('#', '#', '#', ' ', ' ', ' ', ' '),
                arrayOf(' ', '#', ' ', ' ', ' ', ' ', ' ')
            )
        )
        chamber.addShape(Hook(), 2)
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.dump()
        chamber.moveRight(chamber.shapeBottomRow)
        chamber.dump()
        chamber.shapeBottomRow = chamber.moveDown(chamber.shapeBottomRow)
        chamber.dump()
        chamber.moveRight(chamber.shapeBottomRow)
        chamber.dump()
        assert(chamber.rows[4].contentEquals(arrayOf(' ', ' ', ' ', ' ', ' ', ' ', '@')))
        assert(chamber.rows[3].contentEquals(arrayOf(' ', '#', ' ', ' ', ' ', ' ', '@')))
        assert(chamber.rows[2].contentEquals(arrayOf('#', '#', '#', ' ', '@', '@', '@')))
        assert(chamber.rows[1].contentEquals(arrayOf(' ', '#', '#', '#', '#', '#', ' ')))
    }
}