fun main() {
    fun part1(input: List<String>): Int {
        val hands = input.parseCards()
        val ordered = hands.sorted()

        return ordered.foldIndexed(0) { i, acc, hand -> acc + hand.bid * (i + 1) }
    }

    fun part2(input: List<String>): Int {
        val hands = input.parseCards(jokerRules = true)
        val ordered = hands.sorted()

        return ordered.foldIndexed(0) { i, acc, hand -> acc + hand.bid * (i + 1) }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)

    val input = readInput("Day07")
    part1(input).println()

    check(part2(testInput) == 5905)
    part2(input).println()
}

data class Hand(
    val cards: List<Card>,
    val bid: Int,
    val jokerRules: Boolean,
) : Comparable<Hand> {
    val type: HandType by lazy { HandType.entries.last { it.predicate(this) } }

    /**
     * Number of jokers in the hand. Respects whether playing with joker rules (part 2)
     */
    val jokerCount: Int by lazy {
        when {
            jokerRules -> cards.count { it.label == 'J' }
            else -> 0
        }
    }

    /**
     * Compare hands, respecting joker rules
     */
    override fun compareTo(other: Hand): Int {
        // compare the hand type
        val typeCompare = type.compareTo(other.type)
        if (typeCompare != 0) return typeCompare

        // then compare cards, starting with the first card that is different
        val cardsCompare = cards.zip(other.cards).map { (a, b) -> a.compareTo(b) }
        return cardsCompare.first { it != 0 }
    }

    override fun toString(): String {
        val cards = cards.joinToString("") { it.label.toString() }
        return "Hand($cards, $bid, $type, numJokers=$jokerCount)"
    }
}

private val rank = "23456789TJQKA".toCharArray()
private val rankWithJokers = "J23456789TQKA".toCharArray()

data class Card(
    val label: Char,
    val jokerRules: Boolean,
) {
    /**
     * Compare cards, respecting joker rules
     */
    operator fun compareTo(other: Card): Int {
        val r = if (jokerRules) rankWithJokers else rank
        val thisRank = r.indexOf(label)
        val otherRank = r.indexOf(other.label)
        return thisRank.compareTo(otherRank)
    }
}

enum class HandType(val predicate: (Hand) -> Boolean) {
    // no other hand type
    HighCard({ true }),

    // two cards have the same value
    OnePair({ hand ->
        hand.cards.groupBy { it.label }.mapValues { it.value.size }
            .any { it.value + hand.jokerCount == 2 }
    }),

    // two different pairs
    TwoPair({ hand ->
        hand.cards.groupBy { it.label }.mapValues { it.value.size }
            .count { it.value + hand.jokerCount == 2 } == 2
    }),

    // three cards have the same value
    ThreeOfAKind({ hand ->
        hand.cards.groupBy { it.label }.mapValues { it.value.size }
            .any { it.value + hand.jokerCount == 3 }
    }),

    // three cards have the same value, and the remaining two cards have the same value
    FullHouse({ hand ->
        val groups = hand.cards.groupBy { it.label }.mapValues { it.value.size }
        // we need two groups with jokers, one with 3 cards and one with 2 cards, and exactly 2 groups excluding jokers
        groups.any { it.value + hand.jokerCount == 3 } &&
            groups.any { it.value + hand.jokerCount == 2 } &&
            groups.filterKeys { if (hand.jokerRules) it != 'J' else true }.count() == 2
    }),

    // four cards have the same value
    FourOfAKind({ hand ->
        val groups = hand.cards.groupBy { it.label }.mapValues { it.value.size }
        groups.any { (if (it.key == 'J') it.value else it.value + hand.jokerCount) == 4 }
    }),

    // all cards have the same value
    FiveOfAKind({ hand ->
        val groups = hand.cards.groupBy { it.label }.mapValues { it.value.size }
        groups.any { (if (it.key == 'J') it.value else it.value + hand.jokerCount) == 5 }
    }),
}

fun List<String>.parseCards(jokerRules: Boolean = false): List<Hand> {
    return this.map { line ->
        val (cards, rank) = line.split(" ")
        Hand(cards.parseHand(jokerRules), rank.toInt(), jokerRules)
    }
}

fun String.parseHand(jokerRules: Boolean): List<Card> = this.toCharArray().map { Card(it, jokerRules) }