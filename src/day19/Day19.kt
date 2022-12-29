package day19

import readInput
import java.util.*
import java.util.regex.Pattern

enum class Material {
    ORE,
    CLAY,
    OBSIDIAN,
    GEODE
}

data class Robot(val product: Material)

data class BuildCost(val ore: Int, val clay: Int, val obsidian: Int)

class Blueprint(description: String) {
    val id: Int
    val buildCosts = EnumMap<Material, BuildCost>(Material::class.java)

    init {
        val pattern = Pattern.compile( """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""")
        val matcher = pattern.matcher(description)
        if (matcher.find()) {
            var matchGroup = 1
            id = matcher.group(matchGroup++).toInt()
            buildCosts[Material.ORE] = BuildCost(matcher.group(matchGroup++).toInt(), 0, 0)
            buildCosts[Material.CLAY] = BuildCost(matcher.group(matchGroup++).toInt(), 0, 0)
            buildCosts[Material.OBSIDIAN] = BuildCost(matcher.group(matchGroup++).toInt(), matcher.group(matchGroup++).toInt(), 0)
            buildCosts[Material.GEODE] = BuildCost(matcher.group(matchGroup++).toInt(), 0, matcher.group(matchGroup++).toInt())
        } else {
            throw Exception("Blueprint description not in expected format.")
        }
    }
}

data class ProductionState(val blueprint: Blueprint, var stepsRemaining: Int,
                           var robots: MutableList<Robot>,
                           var materials: EnumMap<Material, Int>
    ) {

    fun collectMaterials() {
        for (r in robots) {
            materials[r.product] = (materials[r.product] ?: 0) + 1
        }
    }

    fun canBuildRobot(robotType: Material): Boolean =
        (materials[Material.ORE] ?: 0) >= (blueprint.buildCosts[robotType]?.ore ?: 0) &&
        (materials[Material.CLAY] ?: 0) >= (blueprint.buildCosts[robotType]?.clay ?: 0) &&
        (materials[Material.OBSIDIAN] ?: 0) >= (blueprint.buildCosts[robotType]?.obsidian ?: 0)

    fun buildRobot(robotType: Material): Boolean {
        if (canBuildRobot(robotType)) {
            materials[Material.ORE] = (materials[Material.ORE] ?: 0) - (blueprint.buildCosts[robotType]?.ore ?: 0)
            materials[Material.CLAY] = (materials[Material.CLAY] ?: 0) - (blueprint.buildCosts[robotType]?.clay ?: 0)
            materials[Material.OBSIDIAN] = (materials[Material.OBSIDIAN] ?: 0) - (blueprint.buildCosts[robotType]?.obsidian ?: 0)
            robots.add(Robot(robotType))
            return true
        }
        return false
    }
}

fun permuteProductionStates(blueprint: Blueprint, steps: Int): List<ProductionState> {
    var states = mutableListOf(ProductionState(blueprint, steps, mutableListOf(Robot(Material.ORE)), EnumMap<Material, Int>(Material::class.java)))
    var terminalStates = mutableListOf<ProductionState>()

    while(states.isNotEmpty()) {
        val state = states.last()
        states.removeLast()
        if (state.stepsRemaining == 0) {
            terminalStates.add(state)
            continue
        }
        for (robot in Material.values()) {
            if (state.canBuildRobot(robot)) {
                var newState = state.copy(stepsRemaining = state.stepsRemaining - 1,
                    robots = state.robots.toMutableList(),
                    materials = state.materials.clone())
                newState.collectMaterials()
                newState.buildRobot(robot)
                states.add(newState)
            }
        }
        var newState = state.copy(stepsRemaining = state.stepsRemaining - 1,
            robots = state.robots.toMutableList(),
            materials = state.materials.clone())
        newState.collectMaterials()
        states.add(newState)
    }

    return terminalStates
}

fun parseBlueprints(input: List<String>): List<Blueprint> {
    var blueprints = mutableListOf<Blueprint>()

    for (description in input) {
        blueprints.add(Blueprint(description.trim()))
    }
    return blueprints
}

fun main() {
    fun part1(input: List<String>): Int {
        var qualitySum = 0
        val blueprints = parseBlueprints(input)

        val blueprint = Blueprint( "Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 6 clay. Each geode robot costs 9 ore and 8 obsidian.")
        val states = permuteProductionStates(blueprint, 15)

        /*
        for (blueprint in blueprints) {
            qualitySum += computeBlueprintQuality(blueprint,24)
        }
        */
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
