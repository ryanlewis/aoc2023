fun main() {
    fun part1(input: List<String>): Int {
        val games = input.toGameList()

        val viableGames = games.filter { game ->
            game.sets.all { set ->
                set.red <= 12 && set.green <= 13 && set.blue <= 14
            }
        }

        return viableGames.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        val games = input.toGameList()

        val powers = games.map { game ->
            val maxRed = game.sets.maxOf { it.red }
            val maxGreen = game.sets.maxOf { it.green }
            val maxBlue = game.sets.maxOf { it.blue }
            maxRed * maxGreen * maxBlue
        }

        return powers.sum()
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()

    check(part2(testInput) == 2286)

    part2(input).println()
}

fun List<String>.toGameList() = map { line ->
    val id = line.substringBefore(":").substringAfter("Game ").toInt()
    val sets = line.substringAfter(": ").split(";")

    val colorSets = sets.map {
        val valueColors = it.trim().split(",")
        var r = 0
        var g = 0
        var b = 0

        for (valueColor in valueColors) {
            val (value, color) = valueColor.trim().split(" ")
            when (color) {
                "red" -> r += value.toInt()
                "green" -> g += value.toInt()
                "blue" -> b += value.toInt()
            }
        }
        ColorSet(r, g, b)
    }
    Game(id, colorSets)
}

data class Game(
    val id: Int,
    val sets: List<ColorSet>,
)

data class ColorSet(
    val red: Int,
    val green: Int,
    val blue: Int,
)
