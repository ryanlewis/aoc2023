import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        val scratchCards = input.toScratchCards()
        return scratchCards.sumOf { it.score }
    }

    fun part2(input: List<String>): Int {
        val scratchCards = input.toScratchCards()
        val copies = mutableMapOf<Int, Int>()

        for ((i, card) in scratchCards.withIndex()) {
            val c = copies.getOrDefault(i, 0)
            repeat(c + 1) {
                var j = i
                var n = card.winningNumbers
                while (n > 0) {
                    copies.compute(++j) { _, v -> (v ?: 0) + 1 }
                    n--
                }
            }
        }

        return scratchCards.size + copies.values.sum()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()

    check(part2(testInput) == 30) { "Expected 30, got ${part2(testInput)}" }

    part2(input).println()
}

data class ScratchCard(
    val id: Int,
    val numbers: Set<Int>,
    private val winningSet: Set<Int>,
) {
    /**
     * The number of winning numbers on this card
     */
    val winningNumbers: Int
        get(): Int = numbers.intersect(winningSet).size

    /**
     * The score of this card
     */
    val score: Int
        get(): Int {
            val n = winningNumbers
            return if (n == 0) 0 else 2.0.pow(n - 1.0).toInt()
        }
}

fun List<String>.toScratchCards(): List<ScratchCard> {
    return map { line ->
        val (cardID, rest) = line.split(":")
        val id = cardID.substringAfter("Card ").trim().toInt()

        val (numberSet, winningSet) = rest.split("|")

        val numbers = numberSet.trim().split(" ").filterNot { it.isBlank() }.map { it.trim().toInt() }
        val winningNumbers = winningSet.trim().split(" ").filterNot { it.isBlank() }.map { it.trim().toInt() }

        ScratchCard(id, numbers.toSet(), winningNumbers.toSet())
    }
}