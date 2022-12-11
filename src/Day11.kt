import java.util.regex.Pattern

data class Item(var worryLevel: Int)

class Monkey(startingItems: List<Item>,
             val inspectEffectOperation: (Int) -> Int,
             val testFunction: (Int) -> Boolean,
             val testSuccessRecipient: Int,
             val testFailureRecipient: Int) {

    var items = mutableListOf<Item>()

    init {
        for (item in startingItems) {
            items.add(item.copy())
        }
    }

    fun inspectItem(item: Item): Int {
        // Apply inspection operation
        item.worryLevel = inspectEffectOperation(item.worryLevel)
        // Apply boredom effect
        item.worryLevel /= 3
        // Test worry level & throw
        if (testFunction(item.worryLevel))
            return testSuccessRecipient
        return testFailureRecipient
    }
}

fun createItems(itemsString: String): List<Item> {
    var items = mutableListOf<Item>()
    val worries = itemsString.split(",")

    for (worry in worries) {
        items.add(Item(worry.trim().toInt()))
    }
    return items
}

fun createOperation(opString: String): (Int) -> Int {
    val equalSplit = opString.split("=")
    val elements = equalSplit[1].trim().split(" ")

    check(elements[0].trim() == "old") // Dirty shortcut :}
    when(elements[1].trim()) {
        "*" -> {
            if (elements[2].trim() == "old")
                return { v -> v * v }
            else
                return { v -> v * elements[2].trim().toInt() }
        }
        "+" -> {
            return { v -> v + elements[2].trim().toInt() }
        }
        else -> throw Exception("Unexpected operator")
    }
}

fun createTest(testString: String): (Int) -> Boolean {
    val pattern = Pattern.compile("""divisible by (\d+)""")
    val matcher = pattern.matcher(testString)
    matcher.find()
    val factor = matcher.group(1).toInt()
    return { v -> v % factor == 0 }
}

fun parseRecipient(recipientString: String): Int {
    val pattern = Pattern.compile("""throw to monkey (\d+)""")
    val matcher = pattern.matcher(recipientString)
    matcher.find()
    return matcher.group(1).toInt()
}

fun createMonkey(inputIt: Iterator<String>): Monkey {
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

    return Monkey(createItems(itemsString), createOperation(operationString), createTest(testString), parseRecipient(successString), parseRecipient(failureString) )
}

fun main() {
    fun part1(input: List<String>): Int {
        val inputIt = input.iterator()
        var monkeys = mutableListOf<Monkey>()

        while(inputIt.hasNext()) {
            val recordLine = inputIt.next().trim()
            if (recordLine.isEmpty()) continue
            check(recordLine.startsWith("Monkey"))
            monkeys.add(createMonkey(inputIt))
        }
        val inspectionCounts = MutableList(monkeys.size) {0}
        for (round in IntRange(1,20)) {
            val monkeyIterator = monkeys.listIterator()
            while (monkeyIterator.hasNext()) {
                val index = monkeyIterator.nextIndex()
                var monkey = monkeyIterator.next()
                inspectionCounts[index] += monkey.items.size
                val itemIterator = monkey.items.iterator()
                while(itemIterator.hasNext()){
                    val item = itemIterator.next()
                    itemIterator.remove()
                    val toMonkey = monkey.inspectItem(item)
                    monkeys[toMonkey].items.add(item)
                }
            }
        }
        val sortedCounts = inspectionCounts.sortedDescending()
        return sortedCounts[0] * sortedCounts[1]
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605)
    check(part2(testInput) == 0)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
