enum class OpponentShape {
    ROCK(),
    PAPER(),
    SCISSORS();

    companion object {
        fun getShape(shape: String): OpponentShape =
            when (shape) {
                "A" -> ROCK
                "B" -> PAPER
                "C" -> SCISSORS
                else -> throw Exception("Unexpected shape")
            }
    }
}

enum class Outcome {
    WIN { override fun score(): Int = 6 },
    LOSE { override fun score(): Int = 0 },
    DRAW { override fun score(): Int = 3 };

    abstract fun score(): Int

    companion object {
        fun getOutcome(outcome: String): Outcome =
            when(outcome) {
                "X" -> LOSE
                "Y" -> DRAW
                "Z" -> WIN
                else -> throw Exception("Unexpected outcome")
            }
    }
}

enum class SelfShape {
    ROCK {
        override fun outcome(opponentShape: OpponentShape): Outcome =
            when(opponentShape) {
                OpponentShape.ROCK -> Outcome.DRAW
                OpponentShape.PAPER -> Outcome.LOSE
                OpponentShape.SCISSORS -> Outcome.WIN
            }
        override fun score(opponentShape: OpponentShape): Int = 1 + outcome(opponentShape).score()
    },
    PAPER {
        override fun outcome(opponentShape: OpponentShape): Outcome =
            when(opponentShape) {
                OpponentShape.ROCK -> Outcome.WIN
                OpponentShape.PAPER -> Outcome.DRAW
                OpponentShape.SCISSORS -> Outcome.LOSE
            }
        override fun score(opponentShape: OpponentShape): Int = 2 + outcome(opponentShape).score()
    },
    SCISSORS {
        override fun outcome(opponentShape: OpponentShape): Outcome =
            when(opponentShape) {
                OpponentShape.ROCK -> Outcome.LOSE
                OpponentShape.PAPER -> Outcome.WIN
                OpponentShape.SCISSORS -> Outcome.DRAW
            }
        override fun score(opponentShape: OpponentShape): Int = 3 + outcome(opponentShape).score()
    };

    abstract fun outcome(opponentShape: OpponentShape): Outcome
    abstract fun score(opponentShape: OpponentShape): Int

    companion object {
        fun getShape(shape: String): SelfShape =
            when (shape) {
                "X" -> ROCK
                "Y" -> PAPER
                "Z" -> SCISSORS
                else -> throw Exception("Unexpected shape")
            }
    }
}

enum class InferredShape {
    ROCK {
        override fun score(outcome: Outcome): Int = 1 + outcome.score()
    },
    PAPER {
        override fun score(outcome: Outcome): Int = 2 + outcome.score()
    },
    SCISSORS {
        override fun score(outcome: Outcome): Int = 3 + outcome.score()
    };

    abstract fun score(outcome: Outcome): Int

    companion object {
       fun getShapeForOutcome(opponentShape: OpponentShape, outcome: Outcome): InferredShape =
           when(opponentShape) {
               OpponentShape.ROCK -> when (outcome) {
                   Outcome.LOSE -> SCISSORS
                   Outcome.DRAW -> ROCK
                   Outcome.WIN -> PAPER
               }
               OpponentShape.PAPER -> when (outcome) {
                   Outcome.LOSE -> ROCK
                   Outcome.DRAW -> PAPER
                   Outcome.WIN -> SCISSORS
               }
               OpponentShape.SCISSORS -> when (outcome) {
                   Outcome.LOSE -> PAPER
                   Outcome.DRAW -> SCISSORS
                   Outcome.WIN -> ROCK
               }
           }
    }
}

fun main() {
    fun computeScore(opponentShape: OpponentShape, selfShape: SelfShape): Int = selfShape.score(opponentShape)

    fun part1(input: List<String>): Int {
        var total = 0

        for (match in input) {
            val shapes = match.split(" ")
            total += computeScore(OpponentShape.getShape(shapes[0]), SelfShape.getShape(shapes[1]))
        }

        return total
    }

    fun part2(input: List<String>): Int {
        var total = 0

        for (match in input) {
            val values = match.split(" ")
            val desiredOutcome = Outcome.getOutcome(values[1])
            val inferredShape = InferredShape.getShapeForOutcome(OpponentShape.getShape(values[0]), desiredOutcome)
            total += inferredShape.score(desiredOutcome)
        }

        return total
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
