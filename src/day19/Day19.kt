package day19

import readInput
import java.util.*
import java.util.regex.Pattern
import kotlin.time.measureTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

enum class Material {
    ORE,
    CLAY,
    OBSIDIAN,
    GEODE
}

data class BuildCost(val ore: Int, val clay: Int, val obsidian: Int)

class Blueprint(description: String) {
    val id: Int
    val buildCosts = EnumMap<Material, BuildCost>(Material::class.java)
    val maxMaterialNeeded = EnumMap<Material, Int>(Material::class.java)

    init {
        val pattern = Pattern.compile( """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""")
        val matcher = pattern.matcher(description)
        if (matcher.find()) {
            var matchGroup = 1
            id = matcher.group(matchGroup++).toInt()
            buildCosts[Material.ORE] = BuildCost(matcher.group(matchGroup++).toInt(), 0, 0)
            buildCosts[Material.CLAY] = BuildCost(matcher.group(matchGroup++).toInt(), 0, 0)
            buildCosts[Material.OBSIDIAN] = BuildCost(matcher.group(matchGroup++).toInt(), matcher.group(matchGroup++).toInt(), 0)
            buildCosts[Material.GEODE] = BuildCost(matcher.group(matchGroup++).toInt(), 0, matcher.group(matchGroup).toInt())

            print("Blueprint $id max materials: ")
            maxMaterialNeeded[Material.ORE] = buildCosts.maxOf{ it.value.ore }
            print("ORE: ${maxMaterialNeeded[Material.ORE]} ")
            maxMaterialNeeded[Material.CLAY] = buildCosts.maxOf{ it.value.clay }
            print("CLAY: ${maxMaterialNeeded[Material.CLAY]} ")
            maxMaterialNeeded[Material.OBSIDIAN] = buildCosts.maxOf{ it.value.obsidian }
            println("OBS: ${maxMaterialNeeded[Material.OBSIDIAN]}")
            maxMaterialNeeded[Material.GEODE] = Int.MAX_VALUE
        } else {
            throw Exception("Blueprint description not in expected format.")
        }
    }
}

data class ProductionState(val blueprint: Blueprint, var stepsRemaining: Int,
                           var robots: EnumMap<Material, Int>,
                           var materials: EnumMap<Material, Int>
    ) {

    fun fingerprint(): String {
        // Fingerprint ID is of this form:
        // <steps><robot counts><material quantities>
        var id = "$stepsRemaining|"

        for (m in Material.values()) {
            id += "%d|".format(robots[m] ?: 0)
        }
        for (m in Material.values()) {
            id += "%d|".format(materials[m] ?: 0)
        }
        return id
    }
    fun collectMaterials() {
        for (m in Material.values()) {
            materials[m] = (materials[m] ?: 0) + (robots[m] ?: 0)
        }
    }

    fun canBuildRobot(robotType: Material): Boolean =
        (materials[Material.ORE] ?: 0) >= (blueprint.buildCosts[robotType]?.ore ?: 0) &&
        (materials[Material.CLAY] ?: 0) >= (blueprint.buildCosts[robotType]?.clay ?: 0) &&
        (materials[Material.OBSIDIAN] ?: 0) >= (blueprint.buildCosts[robotType]?.obsidian ?: 0)

    fun shouldBuildRobot(robotType: Material): Boolean =
        when(robotType) {
            Material.ORE -> (robots[Material.ORE] ?: 0) < blueprint.maxMaterialNeeded[Material.ORE]!! * 2
            Material.CLAY -> (robots[Material.CLAY] ?: 0) < blueprint.maxMaterialNeeded[Material.CLAY]!!
            Material.OBSIDIAN -> (robots[Material.OBSIDIAN] ?: 0) < blueprint.maxMaterialNeeded[Material.OBSIDIAN]!!
            Material.GEODE -> true
        }

    fun buildRobot(robotType: Material): Boolean {
        if (canBuildRobot(robotType)) {
            materials[Material.ORE] = (materials[Material.ORE] ?: 0) - (blueprint.buildCosts[robotType]?.ore ?: 0)
            materials[Material.CLAY] = (materials[Material.CLAY] ?: 0) - (blueprint.buildCosts[robotType]?.clay ?: 0)
            materials[Material.OBSIDIAN] = (materials[Material.OBSIDIAN] ?: 0) - (blueprint.buildCosts[robotType]?.obsidian ?: 0)
            robots[robotType] = (robots[robotType] ?: 0) + 1
            return true
        }
        return false
    }
}

fun computeMaxGeodeProduction(blueprint: Blueprint, steps: Int): Int {
    val states = mutableListOf(ProductionState(blueprint, steps, EnumMap<Material, Int>(Material::class.java), EnumMap<Material, Int>(Material::class.java)))
    val alreadyComputedStates = mutableSetOf<String>()
    var maxGeodes = 0

    states.last().robots[Material.ORE] = 1

    while(states.isNotEmpty()) {
        val state = states.last()
        states.removeLast()
        if (state.stepsRemaining == 0) {
            if (state.materials.containsKey(Material.GEODE) && state.materials[Material.GEODE]!! > maxGeodes)
            {
                maxGeodes = state.materials[Material.GEODE]!!
            }
            continue
        }
        for (robot in Material.values()) {
            if (state.canBuildRobot(robot)) {
                if (state.shouldBuildRobot(robot)) {
                    val newState = state.copy(
                        stepsRemaining = state.stepsRemaining - 1,
                        robots = state.robots.clone(),
                        materials = state.materials.clone()
                    )
                    newState.collectMaterials()
                    newState.buildRobot(robot)
                    val fingerprint = newState.fingerprint()
                    if (!alreadyComputedStates.contains(fingerprint)) {
                        states.add(newState)
                        alreadyComputedStates.add(fingerprint)
                    }
                }
            }
        }
        val newState = state.copy(stepsRemaining = state.stepsRemaining - 1,
            robots = state.robots.clone(),
            materials = state.materials.clone())
        newState.collectMaterials()
        val fingerprint = newState.fingerprint()
        if (! alreadyComputedStates.contains(fingerprint)) {
            states.add(newState)
            alreadyComputedStates.add(fingerprint)
        }
    }

    return maxGeodes
}

fun parseBlueprints(input: List<String>): List<Blueprint> {
    val blueprints = mutableListOf<Blueprint>()

    for (description in input) {
        blueprints.add(Blueprint(description.trim()))
    }
    return blueprints
}

@OptIn(ExperimentalTime::class)
fun main() {
    fun part1(input: List<String>): Int {
        var qualitySum = 0
        val blueprints = parseBlueprints(input)

        for (blueprint in blueprints) {
            var maxGeodes = 0
            val elapsedTime: Duration = measureTime {
                maxGeodes = computeMaxGeodeProduction(blueprint, 24)
            }
            println("Blueprint ${blueprint.id} can produce ${maxGeodes}; computation time: ${elapsedTime.toIsoString()}")
            qualitySum += maxGeodes * blueprint.id
        }
        println("Sum of all blueprint qualities: ${qualitySum}")
        return qualitySum
    }

    fun part2(input: List<String>): Int = 0

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 33)
    check(part2(testInput) == 0)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
