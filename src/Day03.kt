import java.lang.Exception

fun main() {
    fun findCommonItem(sack: String): Char {
        val midPoint = sack.length/2

        for (itemIndex in IntRange(0, midPoint-1)) {
            var item = sack[itemIndex]

            if (item in sack.slice(IntRange(midPoint, sack.length - 1))) {
                return item
            }
        }
        throw Exception("No common item found")
    }

    fun findCommonItem(sack1: String, sack2: String, sack3: String): Char {
        for (item in sack1) {
            if (item in sack2 && item in sack3)
                return item
        }
        throw Exception("No common item found")
    }

    fun getItemPriority(item: Char): Int {
        if (item in 'a'..'z')
            return item - 'a' + 1
        return item - 'A' + 27
    }

    fun part1(input: List<String>): Int {
        var total = 0
        for (sack in input) {
            val commonItem = findCommonItem(sack)
            val priority = getItemPriority(commonItem)
            total += priority
        }
        return total
    }

    fun part2(input: List<String>): Int {
        var total = 0
        var it = input.iterator()

        while (it.hasNext()) {
            val sack1 = it.next()
            val sack2 = it.next()
            val sack3 = it.next()
            val commonItem = findCommonItem(sack1, sack2, sack3)
            total += getItemPriority(commonItem)
        }
        return total
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
