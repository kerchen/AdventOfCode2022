
data class Instruction(val duration: Int, val delta: Int)

fun main() {
    fun part1(input: List<String>): Int {
        var signalStrengthSum = 0
        var x = 1
        var counter = 0
        var cycle = 0
        var instruction: Instruction
        val signalSampleCycles = listOf(20, 60, 100, 140, 180, 220)
        val signalSampleCycleIt = signalSampleCycles.iterator()
        var nextSampleCycle = signalSampleCycleIt.next()

        for (instructionString in input) {
            counter += 1
            instruction = if (instructionString.startsWith("noop")) {
                Instruction(1, 0)
            } else {
                Instruction(2, instructionString.split(" ")[1].toInt())
            }
            val instructionEndCycle = cycle + instruction.duration
            if (nextSampleCycle in cycle..instructionEndCycle){
                // Instruction will complete after the end of a sample cycle, so use
                // pre-instruction value of x for signal strength.
                signalStrengthSum += x * nextSampleCycle
                x += instruction.delta
                if (!signalSampleCycleIt.hasNext()) break
                nextSampleCycle = signalSampleCycleIt.next()
            } else {
                x += instruction.delta
            }
            cycle += instruction.duration
        }
        return signalStrengthSum
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(part2(testInput) == 0)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
