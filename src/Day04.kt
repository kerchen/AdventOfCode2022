import java.lang.Exception

class Section(sectionRange: String) {
    var startID: Int = 0
    var endID: Int = 0


    init {
        val idRange = sectionRange.split("-")
        startID = idRange[0].toInt()
        endID = idRange[1].toInt()
    }

    fun isFullyContainedIn(other: Section): Boolean = startID >= other.startID && endID <= other.endID

    fun isOverlappingWith(other: Section): Boolean = startID >= other.startID && startID <= other.endID
}


fun main() {
    fun part1(input: List<String>): Int {
        var total = 0

        for (elfPair in input) {
            val sections = elfPair.split(",")
            val leftSection = Section(sections[0])
            val rightSection = Section(sections[1])

            if (leftSection.isFullyContainedIn(rightSection) || rightSection.isFullyContainedIn(leftSection))
                total += 1
        }
        return total
    }

    fun part2(input: List<String>): Int {
        var total = 0
        for (elfPair in input) {
            val sections = elfPair.split(",")
            val leftSection = Section(sections[0])
            val rightSection = Section(sections[1])

            if (leftSection.isOverlappingWith(rightSection) || rightSection.isOverlappingWith(leftSection))
                total += 1
        }
        return total
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
