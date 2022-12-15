package day13

import readInput

fun findClosingBracket(input: String, start: Int): Int {
    var i = start
    var nestLevel = 1

    while (nestLevel != 0) {
        when(input[i]) {
            '[' -> nestLevel += 1
            ']' -> nestLevel -= 1
        }
        i += 1
    }
    return i
}

    abstract class SequenceItem {
        abstract fun isAtomic(): Boolean
        abstract fun convertToList(): SequenceList
    }

    class SequenceNumber(val number: Int) : SequenceItem() {
        override fun isAtomic(): Boolean {
            return true
        }

        override fun convertToList(): SequenceList {
            return SequenceList("$number")
        }
    }

    class SequenceList(input: String) : SequenceItem() {
        var items = mutableListOf<SequenceItem>()

        override fun isAtomic(): Boolean {
            return false
        }

        override fun convertToList(): SequenceList {
            return this
        }

        init {
            var start = 0

            while ( start < input.length ) {
                when(input[start]) {
                    '[' -> {
                        var end = findClosingBracket(input, start + 1)
                        items.add(SequenceList(input.substring(start+1, end-1)))
                        start = end
                    }
                    ',' -> {
                        start += 1
                    }
                    else -> {
                        var number = 0
                        while(start < input.length && input[start].isDigit()) {
                            number = number * 10 + input[start].digitToInt()
                            start += 1
                        }
                        items.add(SequenceNumber(number))
                    }
                }
            }
        }
    }

    class Packet(input: String, val isDivider: Boolean = false) {
        val sequence = SequenceList(input)

    }

enum class Ordered {
    PUNT,
    YES,
    NO
}

fun isWellOrdered(leftList: SequenceList, rightList: SequenceList): Ordered
{
    val leftIterator = leftList.items.iterator()
    val rightIterator = rightList.items.iterator()

    while (leftIterator.hasNext() && rightIterator.hasNext())
    {
        val leftVal = leftIterator.next()
        val rightVal = rightIterator.next()

        if (leftVal.isAtomic() && rightVal.isAtomic()) {
            val leftNumber = (leftVal as SequenceNumber).number
            val rightNumber = (rightVal as SequenceNumber).number
            if (leftNumber < rightNumber) return Ordered.YES
            if (leftNumber > rightNumber) return Ordered.NO
        } else if (!leftVal.isAtomic() && !rightVal.isAtomic()) {
            val subResult = isWellOrdered(leftVal.convertToList(), rightVal.convertToList())
            if (subResult != Ordered.PUNT)
                return subResult
        } else if (!leftVal.isAtomic()) {
            val subResult = isWellOrdered(leftVal as SequenceList, rightVal.convertToList())
            if (subResult != Ordered.PUNT)
                return subResult
        } else {
            val subResult = isWellOrdered(leftVal.convertToList(), rightVal.convertToList())
            if (subResult != Ordered.PUNT)
                return subResult
        }
    }

    if (!leftIterator.hasNext()) {
        if (rightIterator.hasNext()) {
            return Ordered.YES
        }
    }
    if (!rightIterator.hasNext()) {
        if (leftIterator.hasNext()) {
            return Ordered.NO
        }
    }
    return Ordered.PUNT
}

fun isWellOrdered(leftSide: Packet, rightSide: Packet): Boolean {
    return isWellOrdered(leftSide.sequence, rightSide.sequence) == Ordered.YES
}

class PacketComparator {
    companion object : Comparator<Packet> {
        override fun compare(a: Packet, b: Packet): Int {
            val lessThan = isWellOrdered(a, b)
            if (lessThan)
                return -1
            return 1
        }
    }
}
fun main() {
    fun part1(input: List<String>): Int {
        val inputIterator = input.iterator()
        var packetIndex = 1
        val wellOrderedPackets = mutableSetOf<Int>()

        while(inputIterator.hasNext()) {
            val leftString = inputIterator.next()
            var leftPacket = Packet(leftString.substring(1, leftString.length - 1))
            val rightString = inputIterator.next()
            var rightPacket = Packet(rightString.substring(1, rightString.length - 1))

            if (isWellOrdered(leftPacket, rightPacket)) {
                wellOrderedPackets.add(packetIndex)
            }
            packetIndex += 1

            // Skip blank separator line if present
            if (inputIterator.hasNext()) {
                inputIterator.next()
            }
        }
        return wellOrderedPackets.sum()
    }

    fun part2(input: List<String>): Int {
        val inputIterator = input.iterator()
        val packets = mutableListOf<Packet>()

        packets.add(Packet("[2]", true))
        packets.add(Packet("[6]", true))

        while(inputIterator.hasNext()) {
            val packetString = inputIterator.next()
            packets.add(Packet(packetString.substring(1, packetString.length - 1)))
            val packetString2 = inputIterator.next()
            packets.add(Packet(packetString2.substring(1, packetString2.length - 1)))

            if (inputIterator.hasNext()) {
                inputIterator.next()
            }
        }
        var decoderKey = 1
        var index = 1
        for (p in packets.sortedWith(PacketComparator)) {
            if (p.isDivider) {
                decoderKey *= index
            }
            index += 1
        }
        return decoderKey
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
