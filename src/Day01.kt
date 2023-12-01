fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input.map { line ->
            line.toCharArray().map { c -> c.digitToIntOrNull() }.filterNotNull()
        }

        return numbers.sumOf { it.first() * 10 + it.last() }
    }

    fun part2(input: List<String>): Int {
        val numbers = input.map {
            it.toNumberList()
        }
        return numbers.sumOf { it.first() * 10 + it.last() }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()

    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    part2(input).println()
}
