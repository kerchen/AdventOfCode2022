
package day15

import readInput
import java.lang.Math.abs
import java.util.regex.Pattern

data class Point(var x: Long, var y: Long) {
    fun manhattanDistance(other: Point): Long {
        return abs(x-other.x) + abs(y-other.y)
    }
}

enum class FieldReading {
    SENSOR,
    BEACON,
    KNOWN_EMPTY
}

class SensorField(input: List<String>, focusRow: Long) {
    var field = mutableMapOf<Point, FieldReading>()

    init {
        for (reading in input) {
            var sensorLocation = Point(0, 0)
            var beaconLocation = Point(0, 0)
            if (parseSensorReading(reading, sensorLocation, beaconLocation)) {
                // Assume beacons & sensors cannot be in the same location
                check(!field.containsKey(sensorLocation) || field[sensorLocation] == FieldReading.KNOWN_EMPTY)
                check(!field.containsKey(beaconLocation) || field[beaconLocation] != FieldReading.SENSOR)
                field[sensorLocation] = FieldReading.SENSOR
                field[beaconLocation] = FieldReading.BEACON
                val range = sensorLocation.manhattanDistance(beaconLocation)

                for (x in LongRange(-range, range)) {
                    val testPoint = Point(sensorLocation.x + x, focusRow)
                    val distance = testPoint.manhattanDistance(sensorLocation)
                    if (!field.containsKey(testPoint) && distance <= range) {
                        field[testPoint] = FieldReading.KNOWN_EMPTY
                    }
                }
            }
        }
    }

}

fun parseSensorReading(reading: String, sensorLocation: Point, beaconLocation: Point): Boolean {
    val pattern = Pattern.compile("""Sensor at x=(\-?\d+), y=(\-?\d+): closest beacon is at x=(\-?\d+), y=(\-?\d+)""")
    val matcher = pattern.matcher(reading)
    val matchFound = matcher.find()
    if (matchFound) {
        sensorLocation.x = matcher.group(1).toLong()
        sensorLocation.y = matcher.group(2).toLong()
        beaconLocation.x = matcher.group(3).toLong()
        beaconLocation.y = matcher.group(4).toLong()
    }
    return matchFound
}

fun main() {
    fun part1(input: List<String>, focusRow: Long): Int {
        var field = SensorField(input, focusRow)
        println("Field size: ${field.field.size}")
        val count = field.field.count{ (k, v) -> k.y == focusRow && v != FieldReading.BEACON }
        println(count)
        return count
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput, 10.toLong()) == 26)
    check(part2(testInput) == 0)

    val input = readInput("Day15")
    // 5543957 Too high
    println(part1(input, 2000000.toLong()))
    println(part2(input))
}
