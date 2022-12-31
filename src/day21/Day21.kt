package day21

import day19.BuildCost
import day19.Material
import readInput
import java.util.regex.Pattern

enum class Operation {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE
}

fun translateOp(op: String): Operation = when(op) {
    "+" -> Operation.ADD
    "-" -> Operation.SUBTRACT
    "/" -> Operation.DIVIDE
    "*" -> Operation.MULTIPLY
    else -> throw Exception("Unexpected operation")
}

data class Job(val lhs: String, val op: Operation, val rhs: String)

fun main() {
    fun part1(input: List<String>): Long {
        var resolvedMonkeys = mutableMapOf<String, Long>()
        var unresolvedMonkeys = mutableMapOf<String, Job>()
        val pattern = Pattern.compile( """([a-z]+) ([+\-*/]) ([a-z]+)""")

        for (entry in input) {
            val dataPair = entry.split(":")
            val name = dataPair[0]
            val job = dataPair[1].trim()
            val matcher = pattern.matcher(job)
            if (matcher.find()) {
                var matchGroup = 1
                unresolvedMonkeys[name] = Job(
                    matcher.group(matchGroup++),
                    translateOp(matcher.group(matchGroup++)),
                    matcher.group(matchGroup)
                )
            } else {
                resolvedMonkeys[name] = job.toLong()
            }
        }
        while(unresolvedMonkeys.containsKey("root")) {
            for (entry in unresolvedMonkeys) {
                val lhs = entry.value.lhs
                val rhs = entry.value.rhs
                if (resolvedMonkeys.containsKey(lhs) &&
                    resolvedMonkeys.containsKey(rhs)) {
                    val resolvedValue = when(entry.value.op) {
                        Operation.ADD -> resolvedMonkeys[lhs]!! + resolvedMonkeys[rhs]!!
                        Operation.DIVIDE -> resolvedMonkeys[lhs]!! / resolvedMonkeys[rhs]!!
                        Operation.MULTIPLY -> resolvedMonkeys[lhs]!! * resolvedMonkeys[rhs]!!
                        Operation.SUBTRACT -> resolvedMonkeys[lhs]!! - resolvedMonkeys[rhs]!!
                    }
                    unresolvedMonkeys.remove(entry.key)
                    resolvedMonkeys[entry.key] = resolvedValue
                    break
                }
            }
        }
        val rootYell = resolvedMonkeys["root"]!!
        println("Root monkey yells $rootYell")
        return rootYell
    }

    fun part2(input: List<String>): Long = 0

    val testInput = readInput("Day21_test")
    check(part1(testInput) == 152.toLong())
    check(part2(testInput) == 0.toLong())

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
