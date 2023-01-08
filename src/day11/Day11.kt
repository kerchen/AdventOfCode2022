package day11

import readInput
import java.util.regex.Pattern

data class Item(var worryLevel: Long) {
    var ownershipHistory = mutableListOf<Int>()
}

class Monkey(startingItems: List<Item>,
             monkeyNumber: Int,
             val inspectEffectOperation: (Long) -> Long,
             val testFactor: Long,
             val testFunction: (Long) -> Boolean,
             private val testSuccessRecipient: Int,
             private val testFailureRecipient: Int) {

    var items = mutableListOf<Item>()

    init {
        for (item in startingItems) {
            items.add(item.copy())
            items.last().ownershipHistory.add(monkeyNumber)
        }
    }

    fun inspectItem(item: Item, worryReductionFn: (Long) -> Long): Int {
        // Apply inspection operation
        item.worryLevel = inspectEffectOperation(item.worryLevel)
        // Apply boredom effect
        item.worryLevel = worryReductionFn(item.worryLevel)
        // Test worry level & throw
        if (testFunction(item.worryLevel))
            return testSuccessRecipient
        return testFailureRecipient
    }
}

fun createItems(itemsString: String): List<Item> {
    val items = mutableListOf<Item>()
    val worries = itemsString.split(",")

    for (worry in worries) {
        items.add(Item(worry.trim().toLong()))
    }
    return items
}

fun createOperation(opString: String): (Long) -> Long {
    val equalSplit = opString.split("=")
    val elements = equalSplit[1].trim().split(" ")

    check(elements[0].trim() == "old") // Dirty shortcut :}
    when(elements[1].trim()) {
        "*" -> {
            if (elements[2].trim() == "old")
                return { v -> v * v }
            else
                return { v -> v * elements[2].trim().toLong() }
        }
        "+" -> {
            return { v -> v + elements[2].trim().toLong() }
        }
        else -> throw Exception("Unexpected operator")
    }
}

fun createTest(testString: String): Pair<Long, (Long) -> Boolean> {
    val pattern = Pattern.compile("""divisible by (\d+)""")
    val matcher = pattern.matcher(testString)
    matcher.find()
    val factor = matcher.group(1).toLong()
    return Pair(factor, { v -> (v % factor) == 0.toLong() })
}

fun parseRecipient(recipientString: String): Int {
    val pattern = Pattern.compile("""throw to monkey (\d+)""")
    val matcher = pattern.matcher(recipientString)
    matcher.find()
    return matcher.group(1).toInt()
}

fun createMonkey(inputIt: Iterator<String>, monkeyNumber: Int): Monkey {
    val itemsLine = inputIt.next().trim()
    check(itemsLine.startsWith("Starting items:"))
    val itemsString = itemsLine.split(":")[1]

    val operationLine = inputIt.next().trim()
    check(operationLine.startsWith("Operation:"))
    val operationString = operationLine.split(":")[1]

    val testLine = inputIt.next().trim()
    check(testLine.startsWith("Test:"))
    val testString = testLine.split(":")[1]

    val successLine = inputIt.next().trim()
    check(successLine.startsWith("If true:"))
    val successString = successLine.split(":")[1]

    val failureLine = inputIt.next().trim()
    check(failureLine.startsWith("If false:"))
    val failureString = failureLine.split(":")[1]

    val (testFactor, testFunction) = createTest(testString)
    return Monkey(createItems(itemsString), monkeyNumber, createOperation(operationString), testFactor, testFunction, parseRecipient(successString), parseRecipient(failureString) )
}

fun computeMonkeyBusiness(input: List<String>, roundCount: Int, simpleWorryReduction: Boolean): Long {
    val inputIt = input.iterator()
    val monkeys = mutableListOf<Monkey>()
    var commonDivisor: Long = 1

    while(inputIt.hasNext()) {
        val recordLine = inputIt.next().trim()
        if (recordLine.isEmpty()) continue
        check(recordLine.startsWith("Monkey"))
        monkeys.add(createMonkey(inputIt, monkeys.size))
        commonDivisor *= monkeys.last().testFactor
    }
    val reductionFn = if (simpleWorryReduction) { worry: Long -> worry / 3 } else { worry: Long -> worry % commonDivisor }
    val inspectionCounts = MutableList(monkeys.size) {0}
    for (round in IntRange(1,roundCount)) {
        val monkeyIterator = monkeys.listIterator()
        while (monkeyIterator.hasNext()) {
            val index = monkeyIterator.nextIndex()
            val monkey = monkeyIterator.next()
            inspectionCounts[index] += monkey.items.size
            val itemIterator = monkey.items.iterator()
            while(itemIterator.hasNext()){
                val item = itemIterator.next()
                itemIterator.remove()
                val toMonkey = monkey.inspectItem(item, reductionFn)
                monkeys[toMonkey].items.add(item)
                item.ownershipHistory.add(toMonkey)
            }
        }
    }
    val sortedCounts = inspectionCounts.sortedDescending()
    return sortedCounts[0].toLong() * sortedCounts[1].toLong()
}

fun main() {
    fun part1(input: List<String>): Long = computeMonkeyBusiness(input, 20, true)

    fun part2(input: List<String>): Long = day11.computeMonkeyBusiness(input, 10000, false)

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605.toLong())
    check(part2(testInput) == 2713310158)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
