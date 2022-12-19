
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

class ConstrainedSensorField(input: List<String>, focusRow: Long, limit: Long) {
    var field = mutableMapOf<Long, FieldReading>()

    init {
        for (reading in input) {
            var sensorLocation = Point(0, 0)
            var beaconLocation = Point(0, 0)
            if (parseSensorReading(reading, sensorLocation, beaconLocation)) {
                // Assume beacons & sensors cannot be in the same location
                if (sensorLocation.y == focusRow && sensorLocation.x in 0 .. limit)
                    field[sensorLocation.x] = FieldReading.SENSOR
                if (beaconLocation.y == focusRow && beaconLocation.x in 0 .. limit)
                    field[beaconLocation.x] = FieldReading.BEACON
                val range = sensorLocation.manhattanDistance(beaconLocation) - abs(sensorLocation.y-focusRow)

                for (x in LongRange(-range, range)) {
                    val testX = sensorLocation.x + x
                    if (testX in 0 .. limit) {
                        if (true) {
                            val distance = abs(testX - sensorLocation.x)
                            if (!field.containsKey(testX) && distance <= range) {
                                field[testX] = FieldReading.KNOWN_EMPTY
                            }
                        }
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
        val count = field.field.count{ (k, v) -> k.y == focusRow && v != FieldReading.BEACON }
        return count
    }

    fun part2(input: List<String>, limit: Long): Long {
        var tuningFrequency = 0.toLong()

        for (focusRow in LongRange(0, limit)) {
            var field = ConstrainedSensorField(input, focusRow, limit)
            //println("Row ${focusRow} size = ${field.field.size}")
            //println(field.field)
            if (field.field.size.toLong() == limit){
                var beaconLocation = Point(-1, focusRow)
                for (x in LongRange(0, limit)) {
                    if (! field.field.containsKey(x)){
                        beaconLocation.x = x
                        break
                    }
                }
                println("Found beacon location: ${beaconLocation.x}, ${beaconLocation.y}")
                check(beaconLocation.x >= 0)
                tuningFrequency = beaconLocation.x * 4000000 + beaconLocation.y
                break
            }
        }
        println("Tuning frequency: $tuningFrequency")
        return tuningFrequency
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput, 10.toLong()) == 26)
    check(part2(testInput, 20) == 56000011.toLong())

    val input = readInput("Day15")
    println(part1(input, 2000000.toLong()))
    println(part2(input, 4000000.toLong()))
}
