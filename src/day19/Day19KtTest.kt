package day19

import org.testng.annotations.Test

class Day19KtTest {
    @Test
    fun `Blueprint parser reads data correctly`() {
        val expectedId = 23
        val expectedOreOre = 3
        val expectedClayOre = 7
        val expectedObsidianOre = 13
        val expectedObsidianClay = 299
        val expectedGeodeOre = 44
        val expectedGeodeObsidian = 9
        val description = "Blueprint $expectedId: Each ore robot costs $expectedOreOre ore. Each clay robot costs $expectedClayOre ore. Each obsidian robot costs $expectedObsidianOre ore and $expectedObsidianClay clay. Each geode robot costs $expectedGeodeOre ore and $expectedGeodeObsidian obsidian."
        val blueprint = Blueprint(description)

        assert(blueprint.id == expectedId)
        assert((blueprint.buildCosts[Material.ORE]?.ore ?: 0) == expectedOreOre)
        assert((blueprint.buildCosts[Material.CLAY]?.ore ?: 0) == expectedClayOre)
        assert((blueprint.buildCosts[Material.OBSIDIAN]?.ore ?: 0) == expectedObsidianOre)
        assert((blueprint.buildCosts[Material.OBSIDIAN]?.clay ?: 0) == expectedObsidianClay)
        assert((blueprint.buildCosts[Material.GEODE]?.ore ?: 0) == expectedGeodeOre)
        assert((blueprint.buildCosts[Material.GEODE]?.obsidian ?: 0) == expectedGeodeObsidian)
    }

    @Test
    fun `All blueprints parsed correctly`() {
        val descriptions = listOf(
            "Blueprint 13: Each ore robot costs 1 ore. Each clay robot costs 6 ore. Each obsidian robot costs 1 ore and 9 clay. Each geode robot costs 1 ore and 11 obsidian.",
            "Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 0 ore and 6 clay. Each geode robot costs 9 ore and 8 obsidian.",
            "Blueprint 9: Each ore robot costs 3 ore. Each clay robot costs 9 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 22 ore and 41 obsidian.",
            "Blueprint 2399: Each ore robot costs 9 ore. Each clay robot costs 7 ore. Each obsidian robot costs 5 ore and 7 clay. Each geode robot costs 333 ore and 5 obsidian."
        )
        val blueprints = parseBlueprints(descriptions)

        assert(blueprints.size == 4)
    }
}
