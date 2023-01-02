package day21

import readInput
import java.util.regex.Pattern

enum class Operation {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    COMPARE
}

fun translateOp(op: String): Operation = when(op) {
    "+" -> Operation.ADD
    "-" -> Operation.SUBTRACT
    "/" -> Operation.DIVIDE
    "*" -> Operation.MULTIPLY
    "=" -> Operation.COMPARE
    else -> throw Exception("Unexpected operation")
}

data class Job(val lhs: String, val op: Operation, val rhs: String)

fun parseMonkeyJobs(input: List<String>, resolvedMonkeys: MutableMap<String, Long>, unresolvedMonkeys: MutableMap<String, Job>) {
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

}

fun resolve(monkey: String, resolvedMonkeys: MutableMap<String, Long>, unresolvedMonkeys: MutableMap<String, Job>): Long {
    while(unresolvedMonkeys.containsKey(monkey)) {
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
                    else -> throw Exception("Unexpected operation")
                }
                unresolvedMonkeys.remove(entry.key)
                resolvedMonkeys[entry.key] = resolvedValue
                break
            }
        }
    }

    return resolvedMonkeys[monkey]!!
}


abstract class TreeNode(val name: String) {
    abstract fun dependsOn(provider: String): Boolean
    abstract fun value(): Long
    abstract fun reverseResolve(unknownValue: String, targetValue: Long): Long

}

class InternalNode(name: String, val op: Operation): TreeNode(name) {
    lateinit var lhs: TreeNode
    lateinit var rhs: TreeNode

    override fun dependsOn(provider: String): Boolean = lhs.dependsOn(provider) || rhs.dependsOn(provider)
    override fun value(): Long = when (op) {
            Operation.ADD -> lhs.value() + rhs.value()
            Operation.DIVIDE -> lhs.value() / rhs.value()
            Operation.MULTIPLY -> lhs.value() * rhs.value()
            Operation.SUBTRACT -> lhs.value() - rhs.value()
            else -> throw Exception("Unexpected operation")
    }

    override fun reverseResolve(unknownValue: String, targetValue: Long): Long {
        if (lhs.dependsOn(unknownValue)) {
            return when(op) {
                Operation.ADD -> lhs.reverseResolve(unknownValue, targetValue - rhs.value())
                Operation.SUBTRACT -> lhs.reverseResolve(unknownValue, targetValue + rhs.value())
                Operation.MULTIPLY -> lhs.reverseResolve(unknownValue, targetValue / rhs.value())
                Operation.DIVIDE -> lhs.reverseResolve(unknownValue, targetValue * rhs.value())
                else -> throw Exception("Unexpected operation")
            }
        } else {
            return when(op) {
                Operation.ADD -> rhs.reverseResolve(unknownValue, targetValue - lhs.value())
                Operation.SUBTRACT -> rhs.reverseResolve(unknownValue, lhs.value() - targetValue)
                Operation.MULTIPLY -> rhs.reverseResolve(unknownValue, targetValue / lhs.value())
                Operation.DIVIDE -> rhs.reverseResolve(unknownValue, lhs.value() / targetValue)
                else -> throw Exception("Unexpected operation")
            }
        }
    }
}

class TerminalNode(name: String, val value: Long): TreeNode(name) {
    override fun dependsOn(provider: String): Boolean = name == provider
    override fun value(): Long = value
    override fun reverseResolve(unknownValue: String, targetValue: Long): Long {
        if (name == unknownValue) {
            return targetValue
        }
        return value
    }
}

fun buildSubTree(rootNode: InternalNode, resolvedMonkeys: MutableMap<String, Long>, unresolvedMonkeys: MutableMap<String, Job>) {
    val rootEntry = unresolvedMonkeys[rootNode.name]!!

    if (resolvedMonkeys.containsKey(rootEntry.rhs)) {
        rootNode.rhs = TerminalNode(rootEntry.rhs, resolvedMonkeys[rootEntry.rhs]!!)
    } else {
        rootNode.rhs = InternalNode(rootEntry.rhs, unresolvedMonkeys[rootEntry.rhs]!!.op)
        buildSubTree(rootNode.rhs as InternalNode, resolvedMonkeys, unresolvedMonkeys)
    }

    if (resolvedMonkeys.containsKey(rootEntry.lhs)) {
        rootNode.lhs = TerminalNode(rootEntry.lhs, resolvedMonkeys[rootEntry.lhs]!!)
    } else {
        rootNode.lhs = InternalNode(rootEntry.lhs, unresolvedMonkeys[rootEntry.lhs]!!.op)
        buildSubTree(rootNode.lhs as InternalNode, resolvedMonkeys, unresolvedMonkeys)
    }
}

fun buildTree(resolvedMonkeys: MutableMap<String, Long>, unresolvedMonkeys: MutableMap<String, Job>): InternalNode {
    val root = InternalNode("root", Operation.ADD)

    buildSubTree(root, resolvedMonkeys, unresolvedMonkeys)
    return root
}



fun main() {
    fun part1(input: List<String>): Long {
        val resolvedMonkeys = mutableMapOf<String, Long>()
        val unresolvedMonkeys = mutableMapOf<String, Job>()
        parseMonkeyJobs(input, resolvedMonkeys, unresolvedMonkeys)
        val rootYell = resolve("root", resolvedMonkeys, unresolvedMonkeys)
        println("Root monkey yells $rootYell")
        return rootYell
    }

    fun part2(input: List<String>): Long {
        var humanSays: Long = 0
        val resolvedMonkeys = mutableMapOf<String, Long>()
        val unresolvedMonkeys = mutableMapOf<String, Job>()
        parseMonkeyJobs(input, resolvedMonkeys, unresolvedMonkeys)
        val dependencyTree = buildTree(resolvedMonkeys, unresolvedMonkeys)
        val humanName = "humn"
        if (dependencyTree.rhs.dependsOn(humanName)) {
            val targetVal = resolve(dependencyTree.lhs.name, resolvedMonkeys, unresolvedMonkeys)
            humanSays = dependencyTree.rhs.reverseResolve(humanName, targetVal)
        } else {
            val targetVal = resolve(dependencyTree.rhs.name, resolvedMonkeys, unresolvedMonkeys)
            humanSays = dependencyTree.lhs.reverseResolve(humanName, targetVal)
        }
        println("Human says $humanSays")
        return humanSays
    }

    val testInput = readInput("Day21_test")
    check(part1(testInput) == 152.toLong())
    check(part2(testInput) == 301.toLong())

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
