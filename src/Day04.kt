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
            val cardCount = 1 + (copies[i] ?: 0)
            repeat(cardCount) {
                var j = i
                repeat(card.winningNumbers) {
                    copies.compute(++j) { _, v -> (v ?: 0) + 1 }
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
    private val winningSet: Set<Int>,
    val numbers: Set<Int>,
) {
    /**
     * The number of winning numbers on this card
     */
    val winningNumbers: Int by lazy { numbers.intersect(winningSet).size }

    /**
     * The score of this card
     */
    val score: Int by lazy {
        when (val n = winningNumbers) {
            0 -> 0
            else -> 2.0.pow(n - 1.0).toInt()
        }
    }
}

private fun List<String>.toScratchCards(): List<ScratchCard> {
    return map { line ->
        val (cardID, rest) = line.split(":")
        val id = cardID.substringAfter("Card ").trim().toInt()

        val (winningSet, numberSet) = rest.split("|")

        val winningNumbers = winningSet.toIntList().toSet()
        val numbers = numberSet.toIntList().toSet()

        ScratchCard(id, winningNumbers, numbers)
    }
}
