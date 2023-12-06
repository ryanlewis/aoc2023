fun main() {
    fun part1(input: List<String>): Int {
        val races = input.parseRaces()
        return races.map { it.waysToWin() }.reduce(Int::times)
    }

    fun part2(input: List<String>): Int {
        val time = input[0].substringAfter("Time:").replace(" ", "").toLong()
        val distance = input[1].substringAfter("Distance:").replace(" ", "").toLong()
        val race = Race(time, distance)
        return race.waysToWin()
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)

    val input = readInput("Day06")
    part1(input).println()

    check(part2(testInput) == 71503)
    part2(input).println()
}

data class Race(
    val time: Long,
    val record: Long,
) {
    /**
     * Calculate the number of ways to win within this race
     */
    fun waysToWin(): Int = (1..time).fold(0) { acc, i ->
        val distanceTravelled = i * (time - i)
        if (distanceTravelled > record) acc + 1 else acc
    }
}

private fun List<String>.parseRaces(): List<Race> {
    val times = this[0].toLongList()
    val distance = this[1].toLongList()
    return times.zip(distance).map { Race(it.first, it.second) }
}
