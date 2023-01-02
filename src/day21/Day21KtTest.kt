package day21

import org.testng.annotations.Test

class Day21KtTest {
    @Test
    fun `reverse resolve simple addition with unknown lhs`() {
        val root = InternalNode("root", Operation.ADD)
        val expectedHumanValue: Long = 15
        val knownValue: Long = 48
        val targetValue: Long = expectedHumanValue + knownValue
        root.lhs = TerminalNode("humn", 0)
        root.rhs = TerminalNode("test", knownValue)

        val humanSays = root.reverseResolve("humn", targetValue)
        assert(humanSays == expectedHumanValue)
    }

    @Test
    fun `reverse resolve simple addition with unknown rhs`() {
        val root = InternalNode("root", Operation.ADD)
        val expectedHumanValue: Long = 33
        val knownValue: Long = 23994
        val targetValue: Long = knownValue + expectedHumanValue
        root.lhs = TerminalNode("test", knownValue)
        root.rhs = TerminalNode("humn", 0)

        val humanSays = root.reverseResolve("humn", targetValue)
        assert(humanSays == expectedHumanValue)
    }

    @Test
    fun `reverse resolve simple subtraction with unknown lhs`() {
        val root = InternalNode("root", Operation.SUBTRACT)
        val expectedHumanValue: Long = 19
        val knownValue: Long = 43
        val targetValue: Long = expectedHumanValue - knownValue
        root.lhs = TerminalNode("humn", 0)
        root.rhs = TerminalNode("test", knownValue)

        val humanSays = root.reverseResolve("humn", targetValue)
        assert(humanSays == expectedHumanValue)
    }

    @Test
    fun `reverse resolve simple subtraction with unknown rhs`() {
        val root = InternalNode("root", Operation.SUBTRACT)
        val expectedHumanValue: Long = 19
        val knownValue: Long = 43
        val targetValue: Long = knownValue - expectedHumanValue
        root.lhs = TerminalNode("test", knownValue)
        root.rhs = TerminalNode("humn", 0)

        val humanSays = root.reverseResolve("humn", targetValue)
        assert(humanSays == expectedHumanValue)
    }

    @Test
    fun `reverse resolve simple multiplication with unknown lhs`() {
        val root = InternalNode("root", Operation.MULTIPLY)
        val expectedHumanValue: Long = -34
        val knownValue: Long = 340
        val targetValue: Long = expectedHumanValue * knownValue
        root.lhs = TerminalNode("humn", 0)
        root.rhs = TerminalNode("test", knownValue)

        val humanSays = root.reverseResolve("humn", targetValue)
        assert(humanSays == expectedHumanValue)
    }

    @Test
    fun `reverse resolve simple multiplication with unknown rhs`() {
        val root = InternalNode("root", Operation.MULTIPLY)
        val expectedHumanValue: Long = -34
        val knownValue: Long = 340
        val targetValue: Long = expectedHumanValue * knownValue
        root.lhs = TerminalNode("test", knownValue)
        root.rhs = TerminalNode("humn", 0)

        val humanSays = root.reverseResolve("humn", targetValue)
        assert(humanSays == expectedHumanValue)
    }

    @Test
    fun `reverse resolve simple division with unknown lhs`() {
        val root = InternalNode("root", Operation.DIVIDE)
        val expectedHumanValue: Long = -340
        val knownValue: Long = 34
        val targetValue: Long = expectedHumanValue / knownValue
        root.lhs = TerminalNode("humn", 0)
        root.rhs = TerminalNode("test", knownValue)

        val humanSays = root.reverseResolve("humn", targetValue)
        assert(humanSays == expectedHumanValue)
    }

    @Test
    fun `reverse resolve simple division with unknown rhs`() {
        val root = InternalNode("root", Operation.DIVIDE)
        val expectedHumanValue: Long = -34
        val knownValue: Long = 340
        val targetValue: Long = knownValue / expectedHumanValue
        root.lhs = TerminalNode("test", knownValue)
        root.rhs = TerminalNode("humn", 0)

        val humanSays = root.reverseResolve("humn", targetValue)
        assert(humanSays == expectedHumanValue)
    }
}
