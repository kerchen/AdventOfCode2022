
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

    fun part2(input: List<String>): List<CharArray> {
        val columnCount = 40
        val rowCount = 6
        val image = List(rowCount) { CharArray(columnCount) { '.' } }
        var x = 1
        var counter = 0
        var cycle = 0
        var imageRow = 0
        var imageColumn = 0
        var instruction: Instruction

        for (instructionString in input) {
            counter += 1
            instruction = if (instructionString.startsWith("noop")) {
                Instruction(1, 0)
            } else {
                Instruction(2, instructionString.split(" ")[1].toInt())
            }
            for (crtCycle in IntRange(cycle, cycle+instruction.duration-1)) {
                if (imageColumn >= x - 1 && imageColumn <= x + 1) {
                    image[imageRow][imageColumn] = '#'
                }
                imageColumn += 1
                if (imageColumn == columnCount)
                {
                    imageColumn = 0
                    imageRow += 1
                }
            }
            x += instruction.delta
            cycle += instruction.duration
        }
        return image
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    val testImage = part2(testInput)
    val expectedImage = listOf(
        "##..##..##..##..##..##..##..##..##..##..",
        "###...###...###...###...###...###...###.",
        "####....####....####....####....####....",
        "#####.....#####.....#####.....#####.....",
        "######......######......######......####",
        "#######.......#######.......#######.....")
    for (row in IntRange(0, 5)) {
        check(String(testImage[row]) == expectedImage[row])
    }

    val input = readInput("Day10")
    println(part1(input))
    val image = part2(input)
    for (row in image) {
        println(row)
    }
}
