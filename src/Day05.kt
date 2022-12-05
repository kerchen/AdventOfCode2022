import java.util.regex.Pattern

class CargoBay(arrangement: List<String>, newerCrane: Boolean = false) {
    val stacks = mutableListOf<ArrayDeque<Char>>()

    init {
        val it = arrangement.iterator()

        // Parse initial layout
        while (it.hasNext()) {
            val line = it.next().trimEnd()
            if (line.isEmpty()) break
            var i = 0
            var stackIndex = 1
            while (i < line.length) {
                if (stackIndex > stacks.size) {
                    stacks.add(ArrayDeque())
                }
                if (line[i] == '[') {
                    stacks[stackIndex-1].addFirst(line[i+1])
                }
                i += 4
                stackIndex += 1
            }
        }

        // Parse moves
        val pattern = Pattern.compile("""\p{Alpha}+ (\d+) \p{Alpha}+ (\d+) \p{Alpha}+ (\d+)""")
        while (it.hasNext()) {
            val line = it.next().trimEnd()
            if (! line.startsWith("move", true)) {
                break
            }
            val matcher = pattern.matcher(line)
            matcher.find()
            var count = matcher.group(1).toInt()
            val fromStack = matcher.group(2).toInt() - 1
            val toStack = matcher.group(3).toInt() - 1
            if (newerCrane) {
                val substack = stacks[fromStack].drop(stacks[fromStack].size-count)
                stacks[toStack].addAll(substack)
                while(count > 0) {
                    stacks[fromStack].removeLast()
                    count -= 1
                }
            } else {
                while (count > 0) {
                    stacks[toStack].addLast(stacks[fromStack].last())
                    stacks[fromStack].removeLast()
                    count -= 1
                }
            }
        }
    }
}

fun main() {
    fun part1(input: List<String>): String {
        var result = ""
        val bay = CargoBay(input)
        var stack = 0
        while (stack < bay.stacks.size) {
            result += bay.stacks[stack].last()
            stack += 1
        }
        return result
    }

    fun part2(input: List<String>): String {
        var result = ""
        val bay = CargoBay(input, true)
        var stack = 0
        while (stack < bay.stacks.size) {
            result += bay.stacks[stack].last()
            stack += 1
        }
        return result
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
