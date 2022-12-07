import java.io.File
import java.util.*


abstract class FileSystemNode(val name: String, var size: Int, val parent: FileSystemNode?): Iterable<FileSystemNode> {
    abstract fun addChild(child: FileSystemNode)
    abstract fun hasChildren(): Boolean
    abstract fun computeSize(): Int
}

class FileSystemFile(name: String, size: Int, parent: FileSystemNode) : FileSystemNode(name, size, parent) {
    override operator fun iterator(): Iterator<FileSystemNode> = Collections.emptyIterator()
    override fun addChild(child: FileSystemNode) {
        TODO("Not yet implemented")
    }
    override fun hasChildren(): Boolean = false
    override fun computeSize(): Int = size
}

class FileSystemDirectory(name: String, parent: FileSystemNode?) : FileSystemNode(name, 0, parent) {
    private var children = mutableListOf<FileSystemNode>()
    override operator fun iterator(): Iterator<FileSystemNode> = children.iterator()
    override fun addChild(child: FileSystemNode) {
        children.add(child)
    }
    override fun hasChildren(): Boolean = children.isNotEmpty()

    override fun computeSize(): Int {
        for (child in children) {
            size += child.computeSize()
        }
        return size
    }
}

class FileSystem(layout: List<String>) {
    var root: FileSystemDirectory = FileSystemDirectory("/", null)
    private var currentDirectory: FileSystemNode = root

    init {
        var it = layout.iterator()
        var lookahead = if (it.hasNext()) it.next() else ""
        while (it.hasNext() || lookahead.isNotEmpty()) {
            val command = if (lookahead.isNotEmpty()) lookahead else it.next()
            var commandOutput = mutableListOf<String>()
            lookahead = if (it.hasNext()) it.next() else ""
            while(!lookahead.startsWith("$") && lookahead.isNotEmpty()) {
                commandOutput.add(lookahead)
                lookahead = if (it.hasNext()) it.next() else ""
            }
            parseCommand(command, commandOutput)
        }
    }


    private fun parseCommand(commandString: String, commandOutput: List<String>) {
        val parsedCommand = commandString.substring(2).split(" ")
        when(parsedCommand[0]) {
            "cd" -> {
                changeDirectory(parsedCommand[1])
            }
            "ls" -> {
                listDirectory(commandOutput)
            }
        }
    }

    private fun changeDirectory(targetDirectory: String) {
        when(targetDirectory) {
            "/" -> {
                currentDirectory = root
            }
            ".." -> {
                currentDirectory = currentDirectory.parent!!
            }
            else -> {
                for (dir in currentDirectory) {
                    if (dir.name == targetDirectory) {
                        currentDirectory = dir
                    }
                }
            }
        }
    }

    private fun listDirectory(listing: List<String>) {
        for (dir in listing) {
            val entry = dir.split(" ")
            if (entry[0] == "dir") {
                currentDirectory.addChild(FileSystemDirectory(entry[1], currentDirectory))
            } else {
                currentDirectory.addChild(FileSystemFile(entry[1], entry[0].toInt(), currentDirectory))
            }
        }
    }

}

fun sumDirectorySizes(root: FileSystemNode, maxSize: Int): Int {
    var sum = 0
    if (root.hasChildren()) {
        if (root.size <= maxSize)
            sum += root.size
        for (child in root) {
            sum += sumDirectorySizes(child, maxSize)
        }
    }

    return sum
}

fun main() {
    fun part1(input: List<String>): Int {
        val fileSystem = FileSystem(input)
        fileSystem.root.computeSize()
        val sum = sumDirectorySizes(fileSystem.root, 100000)

        return sum
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day07_test")
    check(FileSystem(testInput).root.computeSize() == 48381165)
    check(part1(testInput) == 95437)
    check(part2(testInput) == 0)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
