fun main() {
    fun part1(input: List<String>): Int {
        var max_calories = 0
        var sum_calories = 0
        var elf = 0

        for (calories in input) {
            val c: Int = try { calories.toInt() } catch (e:NumberFormatException) { 0 }
            if (c != 0) {
                sum_calories += c
            } else {
                if (sum_calories > max_calories) {
                    max_calories = sum_calories
                }
                sum_calories = 0
                elf += 1
            }
        }

        return max_calories
    }

    fun part2(input: List<String>): Int {
        val elf_calories = mutableListOf<Int>()
        var elf = 0

        elf_calories.add(0)

        for (calories in input) {
            val c: Int = try { calories.toInt() } catch (e:NumberFormatException) { 0 }
            if (c != 0) {
                elf_calories[elf] += c
            } else {
                elf_calories.add(0)
                elf += 1
            }
        }

        val sorted_elf_calories = elf_calories.sortedDescending()
        return sorted_elf_calories[0] + sorted_elf_calories[1] + sorted_elf_calories[2]
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("Day01_test")
    //check(part1(testInput) == 1)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
