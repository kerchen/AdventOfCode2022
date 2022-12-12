
class Node(val id: Int) {
    var shortestPath = mutableListOf<Node>()
    var distanceToSource = Integer.MAX_VALUE;
    var adjacentNodes = mutableMapOf<Node, Int>()
}

fun getLowestDistanceNode(unsettledNodes: Set<Node>): Node {
    var lowestDistanceNode = unsettledNodes.first()
    var lowestDistance = lowestDistanceNode.distanceToSource
    for (node in unsettledNodes) {
        if (node.distanceToSource < lowestDistance) {
            lowestDistance = node.distanceToSource
            lowestDistanceNode = node
        }
    }
    return lowestDistanceNode
}

fun calculateMinimumDistance(evaluationNode: Node, weight: Int, sourceNode: Node) {
    if (sourceNode.distanceToSource + weight < evaluationNode.distanceToSource) {
        evaluationNode.distanceToSource = sourceNode.distanceToSource + weight
        var shortestPath = mutableListOf<Node>()
        shortestPath.addAll(sourceNode.shortestPath)
        shortestPath.add(sourceNode)
        evaluationNode.shortestPath = shortestPath
    }
}

class Graph(heights: Day12.HeightMap) {
    var nodes = HashSet<Node>()
    var nodeMap = mutableMapOf<Int, Node>()

    fun generateID(x: Int, y: Int): Int = x + y * 256

    init {
        fun setAdjacent(x: Int, y: Int, nodeMap: Map<Int, Node>, node: Node, height: Int) {
            val adjacentHeight = heights.rows[y][x]
            var adjacentNode = nodeMap[generateID(x, y)]

            if (adjacentHeight <= height + 1) {
                adjacentNode?.let {
                    node.adjacentNodes[adjacentNode] = 1
                }
            }
            if (height <= adjacentHeight + 1) {
                adjacentNode?.let {
                    adjacentNode.adjacentNodes[node] = 1
                }
            }
        }

        for (x in IntRange(0, heights.columnCount-1)) {
            for (y in IntRange(0, heights.rowCount-1)) {
                val id = generateID(x, y)
                val node = Node(id)
                nodes.add(node)
                nodeMap[id] = node
                val cellHeight = heights.rows[y][x]
                if (x > 0 ) {
                    setAdjacent(x-1, y, nodeMap, node, cellHeight)
                }
                if (y > 0 ) {
                    setAdjacent(x, y-1, nodeMap, node, cellHeight)
                }
            }
        }
    }

    fun findShortestPath(startPosition: Day12.Position, endPosition: Day12.Position): Int {
        var startNode = nodeMap[generateID(startPosition.x, startPosition.y)]
        var endNode = nodeMap[generateID(endPosition.x, endPosition.y)]

        if (startNode == null || endNode == null) {
            return Integer.MAX_VALUE
        }

        startNode.distanceToSource = 0

        var settledNodes = HashSet<Node>()
        var unsettledNodes = HashSet<Node>()

        unsettledNodes.add(startNode)

        while (unsettledNodes.isNotEmpty()) {
            var currentNode = getLowestDistanceNode(unsettledNodes)
            unsettledNodes.remove(currentNode)
            for (adjacencyPair in currentNode.adjacentNodes) {
                var adjacentNode = adjacencyPair.key
                val edgeWeight = adjacencyPair.value
                if (adjacentNode !in settledNodes) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode)
                    unsettledNodes.add(adjacentNode)
                }
            }
            settledNodes.add(currentNode)
            if (currentNode == endNode) {
                return currentNode.distanceToSource
            }
        }

        return Integer.MAX_VALUE
    }
}

class Day12 {
    data class Position(var x: Int, var y: Int)

    class HeightMap(input: List<String>) {
        var rows = mutableListOf<List<Int>>()
        var rowCount = 0
        var columnCount = 0
        var startingPosition = Position(0, 0)
        var goalPosition = Position(0, 0)

        init {
            for (row in input) {
                if ("S" in row) {
                    val index = row.indexOf('S')
                    startingPosition.x = index
                    startingPosition.y = rows.size
                }
                if ("E" in row) {
                    val index = row.indexOf('E')
                    goalPosition.x = index
                    goalPosition.y = rows.size
                }
                rows.add(row.map {
                    when (it) {
                        'S' -> 0
                        'E' -> 25
                        else -> it.code - 'a'.code
                    }
                }
                )
            }
            rowCount = rows.size
            columnCount = rows[0].size
        }
    }

}

fun main() {
    fun part1(input: List<String>): Int {
        var heightMap = Day12.HeightMap(input)
        println("Starting pos: ${heightMap.startingPosition}")
        println("Goal pos: ${heightMap.goalPosition}")
        var graph = Graph(heightMap)
        val distance = graph.findShortestPath(heightMap.startingPosition, heightMap.goalPosition)
        println(distance)
        return distance
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 0)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
