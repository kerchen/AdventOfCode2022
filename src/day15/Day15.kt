
package day15

import readInput
import java.lang.Math.abs
import java.util.*
import java.util.regex.Pattern
import javax.swing.Box

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

fun parseInput(input: List<String>, readings: MutableList<Pair<Point, Point>>) {
    for (reading in input) {
        var sensorLocation = Point(0, 0)
        var beaconLocation = Point(0, 0)
        if (parseSensorReading(reading, sensorLocation, beaconLocation)) {
            readings.add(Pair(sensorLocation, beaconLocation))
        }
    }
}

class ConstrainedSensorField(readings: List<Pair<Point, Point>>, focusRow: Long, limit: Long) {
    var field: BitSet = BitSet(limit.toInt()+1)
    var setCount = 0

    init {
        for (reading in readings) {
            val sensorLocation = reading.first
            val beaconLocation = reading.second
            // Assume beacons & sensors cannot be in the same location
            if (sensorLocation.y == focusRow && sensorLocation.x in 0 .. limit) {
                if (! field.get(sensorLocation.x.toInt())) {
                    field.set(sensorLocation.x.toInt())
                    setCount += 1
                }
            }
            if (beaconLocation.y == focusRow && beaconLocation.x in 0 .. limit) {
                if (! field.get(beaconLocation.x.toInt())) {
                    field.set(beaconLocation.x.toInt())
                    setCount += 1
                }
            }
            val range = sensorLocation.manhattanDistance(beaconLocation) - abs(sensorLocation.y-focusRow)

            for (x in LongRange(-range, range)) {
                val testX = sensorLocation.x + x
                if (testX in 0 .. limit) {
                    val distance = abs(testX - sensorLocation.x)
                    if (distance <= range && ! field.get(testX.toInt())) {
                        field.set(testX.toInt())
                        setCount += 1
                        if (setCount > limit)
                            break
                    }
                }
            }
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
            sensorBounds.add(Sensor(sensor, distance.toInt()))
        }
    }

    fun findOutside(x: Int, limit: Int): Int {
        var testPoint = Point(x.toLong(), 0)
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
        return testPoint.y.toInt()
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

    fun part2(input: List<String>, limit: Int): Long {
        var tuningFrequency = 0.toLong()
        var readings = mutableListOf<Pair<Point, Point>>()

        parseInput(input, readings)
        var field = BoundingSensorField(readings)
        var beaconLocation = Point(-1, -1)
        for (x in IntRange(0, limit)) {
            beaconLocation.x = x.toLong()
            beaconLocation.y = field.findOutside(x, limit).toLong()

            if (beaconLocation.y <= limit) {
                println("Found beacon location: ${beaconLocation.x}, ${beaconLocation.y}")
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
    println(part2(input, 4000000))
}
