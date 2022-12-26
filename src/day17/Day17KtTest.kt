package day17

import org.testng.annotations.Test

class Day17KtTest {

    @Test
    fun `Array of spaces converts to zero`() {
        val array = arrayOf(' ', ' ', ' ', ' ')
        assert(array.toInt() == 0)
    }

    @Test
    fun `Array of 7 non-spaces converts to 127`() {
        val array = arrayOf('#', '#', '#', '#', '#', '#', '#')
        assert(array.toInt() == 127)
    }

    @Test
    fun `Array of alternating non-spaces converts to 85`() {
        val array = arrayOf('#', ' ', '#', ' ', '#', ' ', '#')
        assert(array.toInt() == 85)
    }
    @Test
    fun `Array of inverted alternating non-spaces converts to 42`() {
        val array = arrayOf(' ', '#', ' ', '#', ' ', '#', ' ')
        assert(array.toInt() == 42)
    }
    @Test
    fun `autocorrelate simple happy path`() {
        val initialNonCorrelatedSignal = listOf(2, 2, 7, 0)
        val repeatingSignal = listOf(1, 2, 3, 4)
        val signal = initialNonCorrelatedSignal + repeatingSignal + repeatingSignal + repeatingSignal
        val result = autocorrelate(signal, 0, 4)

        println(result)
        assert(result.first == initialNonCorrelatedSignal.size + repeatingSignal.size)
        assert(result.second == 4)
    }

    @Test
    fun `autocorrelate non-zero offset happy path`() {
        val initialNonCorrelatedSignal = listOf(2, 2, 7, 0)
        val repeatingSignal = listOf(1, 2, 3, 4)
        val signal = initialNonCorrelatedSignal + repeatingSignal + repeatingSignal + repeatingSignal
        val result = autocorrelate(signal, 3, 4)

        println(result)
        assert(result.first == initialNonCorrelatedSignal.size + repeatingSignal.size)
        assert(result.second == 4)
    }

    @Test
    fun `autocorrelate long initial non-correlation`() {
        val initialNonCorrelatedSignal = listOf(2, 2, 7, 0, 3, 7, 23, 19, 2, 3, 5, 6, 7)
        val repeatingSignal = listOf(1, 2, 3, 4, 1, 2 ,3, 4, 1, 2, 3, 4)
        val signal = initialNonCorrelatedSignal + repeatingSignal
        val result = autocorrelate(signal, 3, 4)

        println(result)
        assert(result.first == initialNonCorrelatedSignal.size + 4)
        assert(result.second == 4)
    }

    @Test
    fun `autocorrelate shorter min sequence length`() {
        val initialNonCorrelatedSignal = listOf(2, 2, 7, 0, 3, 7, 23, 19, 2, 1, 5, 6, 7)
        val repeatingSignal = listOf(1, 2, 3, 4)
        val signal = initialNonCorrelatedSignal + repeatingSignal + repeatingSignal + repeatingSignal
        val minSequenceLength = 2
        val result = autocorrelate(signal, 1, minSequenceLength)

        println(result)
        assert(result.first == initialNonCorrelatedSignal.size + repeatingSignal.size)
        assert(result.second == repeatingSignal.size)
    }
}