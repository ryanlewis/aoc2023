fun main() {
    fun part1(input: List<String>): Int {
        val almanac = input.parseAlmanac()
        val lowest = almanac.seeds.map { almanac[it] }.minOf { it }
        return lowest.toInt()
    }

    fun part2(input: List<String>): Int {
        val almanac = input.parseAlmanac()
        val seedRanges = almanac.seeds.chunked(2).map { it[0]..it[0] + it[1] }

        // loop each range, find the lowest value
        var lowest = Long.MAX_VALUE
        for (r in seedRanges) {
            for (i in r) {
                val v = almanac[i]
                if (v < lowest) {
                    lowest = v
                }
            }
        }

        return lowest.toInt()
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35)

    val input = readInput("Day05")
    part1(input).println()

    check(part2(testInput) == 46) { "Expected 46, got ${part2(testInput)}" }
    part2(input).println()
}

data class Almanac(
    val seeds: Set<Long>,
    val stack: List<List<SeedRange>>,
) {
    operator fun get(n: Long): Long {
        var d = n
        for (seedRanges in stack) {
            val range = seedRanges.find { d in it }
            if (range != null) {
                d += range.modifier
            }
        }
        return d
    }
}

data class SeedRange(
    private val range: LongRange,
    val modifier: Long,
) {
    operator fun contains(n: Long): Boolean = n in range
}

fun List<String>.parseAlmanac(): Almanac {
    val seeds = this[0].substringAfter("seeds: ").split(' ').map { it.toLong() }.toSet()

    // start processing after header
    var ln = 2

    val stack = mutableListOf<List<SeedRange>>()

    while (ln < this.size && this[ln].isNotBlank()) {
        // ignore map line, isn't useful to us
        ln++

        val ranges = mutableListOf<SeedRange>()

        while (ln < this.size && this[ln].isNotBlank()) {
            val numbers = this[ln].split(' ').map { it.toLong() }
            val destStart = numbers[0]
            val sourceStart = numbers[1]
            val rangeLength = numbers[2]

            val sourceRange = sourceStart until (sourceStart + rangeLength)

            ranges.add(SeedRange(range = sourceRange, modifier = destStart - sourceStart))

            ln++
        }

        stack.add(ranges)
        ln++
    }

    return Almanac(seeds, stack)
}