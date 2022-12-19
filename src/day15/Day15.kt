
package day15

import readInput
import java.lang.Math.abs
import java.util.regex.Pattern

data class Point(var x: Int, var y: Int) {
    fun manhattanDistance(other: Point): Int {
        return abs(x-other.x) + abs(y-other.y)
    }
}

enum class FieldReading {
    SENSOR,
    BEACON,
    KNOWN_EMPTY
}

class SensorField(input: List<String>, focusRow: Int) {
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

                for (x in IntRange(-range, range)) {
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

fun parseInput(input: List<String>, readings: MutableList<Pair<Point, Point>>) {
    for (reading in input) {
        var sensorLocation = Point(0, 0)
        var beaconLocation = Point(0, 0)
        if (parseSensorReading(reading, sensorLocation, beaconLocation)) {
            readings.add(Pair(sensorLocation, beaconLocation))
        }
    }
}


class Sensor(p: Point, d: Int) {
    val sensor = p.copy()
    val distance = d
    fun isInside(p: Point): Boolean = sensor.manhattanDistance(p) <= distance
}
class BoundingSensorField(readings: List<Pair<Point, Point>>) {
    var sensorBounds = mutableListOf<Sensor>()

    init {
        for (reading in readings) {
            val sensor = reading.first
            val beacon = reading.second
            val distance = sensor.manhattanDistance(beacon)
            sensorBounds.add(Sensor(sensor, distance))
        }
    }

    fun findOutside(x: Int, limit: Int): Int {
        var testPoint = Point(x, 0)
        var found = true
        while (testPoint.y <= limit && found) {
            found = false
            for (b in sensorBounds) {
                if (b.isInside(testPoint)) {
                    testPoint.y = b.sensor.y + b.distance - abs(b.sensor.x - testPoint.x) + 1
                    found = true
                    break
                }
            }
        }
        return testPoint.y
    }
}
fun parseSensorReading(reading: String, sensorLocation: Point, beaconLocation: Point): Boolean {
    val pattern = Pattern.compile("""Sensor at x=(\-?\d+), y=(\-?\d+): closest beacon is at x=(\-?\d+), y=(\-?\d+)""")
    val matcher = pattern.matcher(reading)
    val matchFound = matcher.find()
    if (matchFound) {
        sensorLocation.x = matcher.group(1).toInt()
        sensorLocation.y = matcher.group(2).toInt()
        beaconLocation.x = matcher.group(3).toInt()
        beaconLocation.y = matcher.group(4).toInt()
    }
    return matchFound
}

fun main() {
    fun part1(input: List<String>, focusRow: Int): Int {
        var field = SensorField(input, focusRow)
        val count = field.field.count{ (k, v) -> k.y == focusRow && v != FieldReading.BEACON }
        return count
    }

    fun part2(input: List<String>, limit: Int): Long {
        var tuningFrequency = 0.toLong()
        var readings = mutableListOf<Pair<Point, Point>>()

        parseInput(input, readings)
        var field = BoundingSensorField(readings)
        var beaconLocation = Point(-1, -1)
        for (x in IntRange(0, limit)) {
            beaconLocation.x = x
            beaconLocation.y = field.findOutside(x, limit)

            if (beaconLocation.y <= limit) {
                println("Found beacon location: ${beaconLocation.x}, ${beaconLocation.y}")
                tuningFrequency = beaconLocation.x.toLong() * 4000000 + beaconLocation.y.toLong()
                break
            }
        }
        println("Tuning frequency: $tuningFrequency")
        return tuningFrequency
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011.toLong())

    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}
