package day20

import org.testng.annotations.Test

class Day20KtTest {
    @Test
    fun `parser test`() {
        val input = listOf("3", "7", "0", "9", "-1339", "39")
        val result = parseInput(input)

        assert(result.size == 6)
        assert(result[0].value == 3)
        assert(result[1].value == 7)
        assert(result[2].value == 0)
        assert(result[3].value == 9)
        assert(result[4].value == -1339)
        assert(result[5].value == 39)
    }

    @Test
    fun `find index finds index of first zero when it is the first entry`() {
        val mixedFile = listOf(Number(0), Number(1), Number(2), Number(3), Number(4))
        val index = findIndex(mixedFile[0], mixedFile)

        assert(index == 0)
    }

    @Test
    fun `find index finds index of first zero when it is the last entry`() {
        val mixedFile = listOf(Number(-1), Number(1), Number(2), Number(3), Number(4), Number(0))
        val index = findIndex(mixedFile[mixedFile.size - 1], mixedFile)

        assert(index == mixedFile.size - 1)
    }

    @Test
    fun `find index finds index of first zero when it is in the middle`() {
        val mixedFile = listOf(Number(-1), Number(1), Number(0), Number(3), Number(4), Number(10))
        val index = findIndex(mixedFile[2], mixedFile)

        assert(index == 2)
    }

    @Test
    fun `find grove coordinate without wrapping`() {
        val mixedFile = listOf(Number(0), Number(1), Number(2), Number(3), Number(4))
        val coordinate = findGroveCoordinate(mixedFile, 3)

        assert(coordinate == 3)
    }

    @Test
    fun `find grove coordinate wrapping once`() {
        val mixedFile = listOf(Number(1), Number(2), Number(0), Number(3), Number(4))
        val coordinate = findGroveCoordinate(mixedFile, 3)

        assert(coordinate == 1)
    }

    @Test
    fun `find grove coordinate wrapping multiple times`() {
        val mixedFile = listOf(Number(1), Number(2), Number(0), Number(3), Number(4))
        val coordinate = findGroveCoordinate(mixedFile, 519)

        assert(coordinate == 2)
    }

    @Test
    fun `first example step 1 moves between 2 and -3`() {
        val numbers = listOf(Number(1), Number(2), Number(-3), Number(3), Number(-2), Number(0), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[0], mixedFile)
        assert(mixedFile == listOf(numbers[1], numbers[0], numbers[2], numbers[3], numbers[4], numbers[5], numbers[6]))
    }

    @Test
    fun `second example step 2 moves between -3 and 3`() {
        val numbers = listOf(Number(2), Number(1), Number(-3), Number(3), Number(-2), Number(0), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[0], mixedFile)
        assert(mixedFile == listOf(numbers[1], numbers[2], numbers[0], numbers[3], numbers[4], numbers[5], numbers[6]))
    }

    @Test
    fun `third example step -3 moves between -2 and 0`() {
        val numbers = listOf(Number(1), Number(-3), Number(2), Number(3), Number(-2), Number(0), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[1], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[2], numbers[3], numbers[4], numbers[1], numbers[5], numbers[6]))
    }

    @Test
    fun `fourth example step 3 moves between 0 and 4`() {
        val numbers = listOf(Number(1), Number(2), Number(3), Number(-2), Number(-3), Number(0), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[3], numbers[4], numbers[5], numbers[2], numbers[6]))
    }

    @Test
    fun `fifth example step -2 moves between 4 and 1`() {
        val numbers = listOf(Number(1), Number(2), Number(-2), Number(-3), Number(0), Number(3), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[2], numbers[0], numbers[1], numbers[3], numbers[4], numbers[5], numbers[6]))
    }

    @Test
    fun `sixth example step 0 does not move`() {
        val numbers = listOf(Number(1), Number(2), Number(-3), Number(0), Number(3), Number(4), Number(-2))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[3], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[2], numbers[3], numbers[4], numbers[5], numbers[6]))
    }

    @Test
    fun `seventh example step 4 moves between -3 and 0`() {
        val numbers = listOf(Number(1), Number(2), Number(-3), Number(0), Number(3), Number(4), Number(-2))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[5], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[2], numbers[5], numbers[3], numbers[4], numbers[6]))
    }

    @Test
    fun `moving zero does not change mixed file`() {
        val numbers = listOf(Number(1), Number(2), Number(0), Number(3), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == numbers)
    }

    @Test
    fun `mix 1 1 2 one step forward from first position moves to second position`() {
        val numbers = listOf(Number(1), Number(2), Number(0), Number(3), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[0], mixedFile)
        assert(mixedFile == listOf(numbers[1], numbers[0], numbers[2], numbers[3], numbers[4]))
    }

    @Test
    fun `mix -1 2 1 one step backward from second position moves to first position`() {
        val numbers = listOf(Number(1), Number(-1), Number(0), Number(3), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[1], mixedFile)
        assert(mixedFile == listOf(numbers[1], numbers[0], numbers[2], numbers[3], numbers[4]))
    }

    @Test
    fun `mix 2 2 4 two steps forward from second position moves to fourth position`() {
        val numbers = listOf(Number(1), Number(2), Number(0), Number(3), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[1], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[2], numbers[3], numbers[1], numbers[4]))
    }

    @Test
    fun `mix 2 4 2 two steps forward from fourth position moves to second position`() {
        val numbers = listOf(Number(1), Number(2), Number(0), Number(2), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[3], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[3], numbers[1], numbers[2], numbers[4]))
    }

    @Test
    fun `mix 3 4 3 three steps forward from fourth position moves to third position`() {
        val numbers = listOf(Number(1), Number(2), Number(0), Number(3), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[3], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[3], numbers[2], numbers[4]))
    }

    @Test
    fun `mix -1 1 5 one step backward from first position moves to fourth position`() {
        val numbers = listOf(Number(-1), Number(0), Number(-1), Number(3), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[0], mixedFile)
        assert(mixedFile == listOf(numbers[1], numbers[2], numbers[3], numbers[0], numbers[4]))
    }

    @Test
    fun `mix -2 1 4 two steps backward from first position moves to third position`() {
        val numbers = listOf(Number(-2), Number(0), Number(-1), Number(3), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[0], mixedFile)
        assert(mixedFile == listOf(numbers[1], numbers[2], numbers[0], numbers[3], numbers[4]))
    }

    @Test
    fun `mix -3 1 3 three steps backward from first position moves to second position`() {
        val numbers = listOf(Number(-3), Number(0), Number(-1), Number(3), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[0], mixedFile)
        assert(mixedFile == listOf(numbers[1], numbers[0], numbers[2], numbers[3], numbers[4]))
    }

    @Test
    fun `mix -1 3 2 one step backward from third position moves to second position`() {
        val numbers = listOf(Number(1), Number(0), Number(-1), Number(3), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[2], numbers[1], numbers[3], numbers[4]))
    }

    @Test
    fun `mix -2 3 1 two steps backward from third position moves to first position`() {
        val numbers = listOf(Number(1), Number(0), Number(-2), Number(3), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[2], numbers[0], numbers[1], numbers[3], numbers[4]))
    }

    @Test
    fun `mix -3 3 4 three steps backward from third position moves to fourth position`() {
        val numbers = listOf(Number(1), Number(2), Number(-3), Number(7), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[3], numbers[2], numbers[4]))
    }

    @Test
    fun `mix -4 3 3 four steps backward from third position moves to third position`() {
        val numbers = listOf(Number(1), Number(2), Number(-4), Number(7), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[2], numbers[3], numbers[4]))
    }

    @Test
    fun `mix -5 3 3 five steps backward from third position moves to second position`() {
        val numbers = listOf(Number(1), Number(2), Number(-5), Number(5), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[2], numbers[1], numbers[3], numbers[4]))
    }

    @Test
    fun `mix 1 3 4 one step forward from third position moves to fourth position`() {
        val numbers = listOf(Number(1), Number(0), Number(1), Number(5), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[3], numbers[2], numbers[4]))
    }

    @Test
    fun `mix 2 3 1 two steps forward from third position moves to first position`() {
        val numbers = listOf(Number(1), Number(0), Number(2), Number(5), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[2], numbers[0], numbers[1], numbers[3], numbers[4]))
    }

    @Test
    fun `mix 3 3 2 three steps forward from third position moves to second position`() {
        val numbers = listOf(Number(1), Number(0), Number(3), Number(5), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[2], numbers[1], numbers[3], numbers[4]))
    }

    @Test
    fun `mix 4 3 3 four steps forward from third position moves to third position`() {
        val numbers = listOf(Number(1), Number(0), Number(4), Number(5), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[2], numbers[3], numbers[4]))
    }

    @Test
    fun `mix 5 3 4 five steps forward from third position moves to fourth position`() {
        val numbers = listOf(Number(1), Number(2), Number(5), Number(89), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[2], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[3], numbers[2], numbers[4]))
    }

    @Test
    fun `mix -5 4 3 five steps backward from fourth position moves to third position`() {
        val numbers = listOf(Number(1), Number(2), Number(0), Number(-5), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[3], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[3], numbers[2], numbers[4]))
    }

    @Test
    fun `mix 7 2 3 seven steps forward from second position moves to first position`() {
        val numbers = listOf(Number(1), Number(7), Number(0), Number(5), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[1], mixedFile)
        assert(mixedFile == listOf(numbers[1], numbers[0], numbers[2], numbers[3], numbers[4]))
    }

    @Test
    fun `mix -7 4 1 seven steps backward from fourth position moves to first position`() {
        val numbers = listOf(Number(1), Number(7), Number(0), Number(-7), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[3], mixedFile)
        assert(mixedFile == listOf(numbers[3], numbers[0], numbers[1], numbers[2], numbers[4]))
    }

    @Test
    fun `mix 60 steps forward from second position stays in second position`() {
        val numbers = listOf(Number(1), Number(60), Number(0), Number(-7), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[1], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[2], numbers[3], numbers[4]))
    }

    @Test
    fun `mix 65 steps forward from second position moves to third position`() {
        val numbers = listOf(Number(1), Number(65), Number(0), Number(-7), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[1], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[2], numbers[1], numbers[3], numbers[4]))
    }

    @Test
    fun `mix 60 steps backward from second position remains in second position`() {
        val numbers = listOf(Number(1), Number(-60), Number(0), Number(-7), Number(4))
        val mixedFile = numbers.toMutableList()

        mix(mixedFile[1], mixedFile)
        assert(mixedFile == listOf(numbers[0], numbers[1], numbers[2], numbers[3], numbers[4]))
    }
}
