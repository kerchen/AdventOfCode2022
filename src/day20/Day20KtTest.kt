package day20

import org.testng.annotations.Test

class Day20KtTest {
    @Test
    fun `parser test`() {
        val input = listOf( "3", "7", "0", "9", "-1339", "39")
        val result = parseInput(input)

        assert(result.equals(listOf(3, 7, 0, 9, -1339, 39)))
    }

    @Test
    fun `find index finds index of first zero when it is the first entry`() {
        val mixedFile = listOf(0, 1, 2, 3, 4)
        val index = findIndex(0, mixedFile)

        assert(index == 0)
    }

    @Test
    fun `find index finds index of first zero when it is the last entry`() {
        val mixedFile = listOf(-1, 1, 2, 3, 4, 0)
        val index = findIndex(0, mixedFile)

        assert(index == mixedFile.size - 1)
    }

    @Test
    fun `find index finds index of first zero when it is in the middle`() {
        val mixedFile = listOf(-1, 1, 0, 3, 4, 10)
        val index = findIndex(0, mixedFile)

        assert(index == 2)
    }

    @Test
    fun `find grove coordinate without wrapping`() {
        val mixedFile = listOf(0, 1, 2, 3, 4)
        val coordinate = findGroveCoordinate(mixedFile, 3)

        assert(coordinate == 3)
    }

    @Test
    fun `find grove coordinate wrapping once`() {
        val mixedFile = listOf(1, 2, 0, 3, 4)
        val coordinate = findGroveCoordinate(mixedFile, 3)

        assert(coordinate == 1)
    }

    @Test
    fun `find grove coordinate wrapping multiple times`() {
        val mixedFile = listOf(1, 2, 0, 3, 4)
        val coordinate = findGroveCoordinate(mixedFile, 519)

        assert(coordinate == 2)
    }

    @Test
    fun `moving zero does not change mixed file`() {
        var mixedFile = mutableListOf(1, 2, 0, 3, 4)

        mix(0, mixedFile)
        assert(mixedFile == listOf(1, 2, 0, 3, 4))
    }

    @Test
    fun `mix one step forward`() {
        var mixedFile = mutableListOf(1, 2, 0, 3, 4)

        mix(1, mixedFile)
        assert(mixedFile == listOf(2, 1, 0, 3, 4))
    }

    @Test
    fun `mix one step backward`() {
        var mixedFile = mutableListOf(1, -1, 0, 3, 4)

        mix(-1, mixedFile)
        assert(mixedFile == listOf(-1, 1, 0, 3, 4))
    }

    @Test
    fun `mix two steps forward`() {
        var mixedFile = mutableListOf(1, 2, 0, 3, 4)

        mix(2, mixedFile)
        assert(mixedFile == listOf(1, 0, 3, 2, 4))
    }

    @Test
    fun `mix two steps backward`() {
        var mixedFile = mutableListOf(1, -2, 0, 3, 4)

        mix(-2, mixedFile)
        assert(mixedFile == listOf(1, 0, 3, -2, 4))
    }

    @Test
    fun `mix three steps forward wraps around before old position`() {
        var mixedFile = mutableListOf(1, 2, 0, 3, 4)

        mix(3, mixedFile)
        assert(mixedFile == listOf(1, 2, 3, 0, 4))
    }

    @Test
    fun `mix three steps backward wraps around after old position`() {
        var mixedFile = mutableListOf(1, 2, -3, 7, 4)

        mix(-3, mixedFile)
        assert(mixedFile == listOf(1, 2, 7, -3, 4))
    }

    @Test
    fun `mix five steps forward wraps around to same position`() {
        var mixedFile = mutableListOf(1, 2, 0, 5, 4)

        mix(5, mixedFile)
        assert(mixedFile == listOf(1, 2, 0, 5, 4))
    }

    @Test
    fun `mix five steps backward wraps around to same position`() {
        var mixedFile = mutableListOf(1, 2, 0, -5, 4)

        mix(-5, mixedFile)
        assert(mixedFile == listOf(1, 2, 0, -5, 4))
    }

    @Test
    fun `mix seven steps forward wraps around after old position`() {
        var mixedFile = mutableListOf(1, 7, 0, 5, 4)

        mix(7, mixedFile)
        assert(mixedFile == listOf(1, 0, 5, 7, 4))
    }

    @Test
    fun `mix seven steps backward wraps around before old position`() {
        var mixedFile = mutableListOf(1, 7, 0, -7, 4)

        mix(-7, mixedFile)
        assert(mixedFile == listOf(1, -7, 7, 0, 4))
    }
}
