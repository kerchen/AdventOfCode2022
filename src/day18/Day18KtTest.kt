package day18

import org.testng.Assert.assertFalse
import org.testng.annotations.Test

class Day18KtTest {
    @Test
    fun `Single cube has surface area = 6`() {
        val cubes = listOf(Point(0, 0, 0))
        assert(computeSurfaceArea(cubes) == 6)
    }

    @Test
    fun `Two connected cubes have surface area = 10`() {
        val cubes = listOf(Point(0, 0, 0), Point(0, 0, 1))
        assert(computeSurfaceArea(cubes) == 10)
    }

    @Test
    fun `Three singly-connected cubes have surface area = 14`() {
        val cubes = listOf(Point(0, 0, 0), Point(0, 0, 1), Point(0, 0, 2))
        assert(computeSurfaceArea(cubes) == 14)
    }

    @Test
    fun `Two x two plane of cubes have surface area = 16`() {
        val cubes = listOf(
            Point(0, 0, 0), Point(1, 0, 0),
            Point(0, 1, 0), Point(1, 1, 0)
        )
        assert(computeSurfaceArea(cubes) == 16)
    }

    @Test
    fun `Three x three plane of cubes have surface area = 30`() {
        val cubes = listOf(
            Point(0, 0, 0), Point(1, 0, 0), Point(2, 0, 0),
            Point(0, 1, 0), Point(1, 1, 0), Point(2, 1, 0),
            Point(0, 2, 0), Point(1, 2, 0), Point(2, 2, 0)
        )
        assert(computeSurfaceArea(cubes) == 30)
    }

    @Test
    fun `Three x three cube of cubes have surface area = 54`() {
        val cubes = listOf(
            Point(0, 0, 0), Point(1, 0, 0), Point(2, 0, 0),
            Point(0, 1, 0), Point(1, 1, 0), Point(2, 1, 0),
            Point(0, 2, 0), Point(1, 2, 0), Point(2, 2, 0),
            Point(0, 0, 1), Point(1, 0, 1), Point(2, 0, 1),
            Point(0, 1, 1), Point(1, 1, 1), Point(2, 1, 1),
            Point(0, 2, 1), Point(1, 2, 1), Point(2, 2, 1),
            Point(0, 0, 2), Point(1, 0, 2), Point(2, 0, 2),
            Point(0, 1, 2), Point(1, 1, 2), Point(2, 1, 2),
            Point(0, 2, 2), Point(1, 2, 2), Point(2, 2, 2)
            )
        assert(computeSurfaceArea(cubes) == 54)
    }

    @Test
    fun `Single cube is not adjacent to itself`() {
        val cube = Point(0, 0, 0)
        assertFalse(areAdjacent(cube, cube))
    }

    @Test
    fun `Two cubes with more than one unit separation in x dimension are not adjacent`() {
        val cube1 = Point(2, 0, 0)
        val cube2 = Point(0, 0, 0)
        assertFalse(areAdjacent(cube1, cube2))
    }
    @Test
    fun `Two cubes with more than one unit separation in y dimension are not adjacent`() {
        val cube1 = Point(0, 0, 0)
        val cube2 = Point(0, 2, 0)
        assertFalse(areAdjacent(cube1, cube2))
    }
    @Test
    fun `Two cubes with more than one unit separation in z dimension are not adjacent`() {
        val cube1 = Point(0, 0, -2)
        val cube2 = Point(0, 0, 0)
        assertFalse(areAdjacent(cube1, cube2))
    }
    @Test
    fun `Two cubes separated by one unit in more than one dimension are not adjacent`() {
        val cube1 = Point(1, 0, 0)
        val cube2 = Point(0, 1, 0)
        assertFalse(areAdjacent(cube1, cube2))
    }

    @Test
    fun `Bounding cuboid for single cube at 0, 0, 0`() {
        val cubes = setOf(Point(0, 0, 0))
        val bounds = computeBounds(cubes)

        assert(bounds.minimum == Point(-1, -1, -1))
        assert(bounds.maximum == Point(1, 1, 1))
    }

    @Test
    fun `Bounding cuboid for star centered on 0, 0, 0`() {
        val cubes = setOf(
            Point(0, 0, 1),
            Point(0, -1, 0),
            Point(0, 1, 0 ),
            Point(1, 0, 0),
            Point(-1, 0, 0),
            Point(0, 0, -1)
            )
        val bounds = computeBounds(cubes)

        assert(bounds.minimum == Point(-2, -2, -2))
        assert(bounds.maximum == Point(2, 2, 2))
    }
    @Test
    fun `Two adjacent cubes have exterior surface area = 10`() {
        val cubes = setOf(Point(0, 0, 0), Point(1, 0, 0))
        assert(computeExteriorSurfaceArea(cubes) == 10)
    }

    @Test
    fun `Star with empty center has exterior surface area = 30`() {
        val cubes = setOf(
            Point(0, 0, 1),
            Point(0, -1, 0),
            Point(0, 1, 0),
            Point(1, 0, 0),
            Point(-1, 0, 0),
            Point(0, 0, -1)
        )
        assert(computeExteriorSurfaceArea(cubes) == 30)
    }
    @Test
    fun `Concave shape has exterior surface area = 30`() {
        val cubes = setOf(
            Point(0, 0, 1),
            Point(0, -1, 0),
            Point(0, 1, 0),
            Point(1, 0, 0),
            Point(-1, 0, 0)
        )
        assert(computeExteriorSurfaceArea(cubes) == 30)
    }
}