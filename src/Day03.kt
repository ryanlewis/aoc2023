fun main() {

    fun part1(input: List<String>): Int {
        val engine = input.toEngineMatrix()

        // loop each engine part, and check to see if there is a symbol adjacent to it in all directions
        val desiredParts = mutableListOf<Part.PartNumber>()
        for (cy in engine.indices) {
            for (part in engine[cy]) {
                if (part is Part.PartNumber) {
                    val len = part.number.toString().length

                    // up
                    if (cy - 1 >= 0) {
                        val symbolAdjacent = engine[cy - 1]
                            .filterIsInstance<Part.Symbol>()
                            .any { it.x in part.x - 1..part.x + len }
                        if (symbolAdjacent) {
                            desiredParts.add(part)
                            continue
                        }
                    }

                    // left
                    if (part.y - 1 >= 0) {
                        val symbolAdjacent = engine[cy]
                            .filterIsInstance<Part.Symbol>()
                            .any { it.x in part.x - 1..part.x + len }
                        if (symbolAdjacent) {
                            desiredParts.add(part)
                            continue
                        }
                    }

                    // right
                    if (part.y + 1 < engine[cy].size) {
                        val symbolAdjacent = engine[cy]
                            .filterIsInstance<Part.Symbol>()
                            .any { it.x in part.x - 1..part.x + len }
                        if (symbolAdjacent) {
                            desiredParts.add(part)
                            continue
                        }
                    }

                    // down
                    if (cy + 1 < engine.size) {
                        val symbolAdjacent = engine[cy + 1]
                            .filterIsInstance<Part.Symbol>()
                            .any { it.x in part.x - 1..part.x + len }
                        if (symbolAdjacent) {
                            desiredParts.add(part)
                            continue
                        }
                    }
                }
            }
        }

        return desiredParts.sumOf { it.number }
    }

    fun part2(input: List<String>): Int {
        val engine = input.toEngineMatrix()
        val gears = engine
            .flatten()
            .filterIsInstance<Part.Symbol>()
            .filter { it.char == '*' }
        val ratios = mutableListOf<Int>()

        for (gear in gears) {
            val candidates = mutableListOf<Part.PartNumber>()
            // up
            if (gear.y - 1 >= 0) {
                val parts = engine[gear.y - 1]
                    .filterIsInstance<Part.PartNumber>()
                    .filter { it.x in gear.x - it.number.toString().length..gear.x + 1 }
                if (parts.any()) {
                    for (part in parts) {
                        candidates.add(part)
                    }
                }
            }

            // down
            if (gear.y + 1 < engine.size) {
                val parts = engine[gear.y + 1]
                    .filterIsInstance<Part.PartNumber>()
                    .filter { it.x in gear.x - it.number.toString().length..gear.x + 1 }
                if (parts.any()) {
                    for (part in parts) {
                        candidates.add(part)
                    }
                }
            }

            // left
            val leftPart = engine[gear.y]
                .filterIsInstance<Part.PartNumber>()
                .find { it.x == gear.x - it.number.toString().length }
            if (leftPart != null) {
                candidates.add(leftPart)
            }

            // right
            val rightPart = engine[gear.y]
                .filterIsInstance<Part.PartNumber>()
                .find { it.x == gear.x + 1 }
            if (rightPart != null) {
                candidates.add(rightPart)
            }

            if (candidates.size == 2) {
                val ratio = candidates[0].number * candidates[1].number
                ratios.add(ratio)
            }
        }

        return ratios.sum()
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()
    check(part1(input) == 535078) { "Expected 535078, got ${part1(input)}" }

    check(part2(testInput) == 467835) { "Expected 467835, got ${part2(testInput)}" }

    part2(input).println()
    check(part2(input) == 75312571) { "Expected 75312571, got ${part2(testInput)}" }
}

sealed class Part(open val x: Int, open val y: Int) {
    data class PartNumber(val number: Int, override val x: Int, override val y: Int) : Part(x, y) {
        override fun toString(): String {
            return "$number(${y + 1}:${x + 1})"
        }
    }

    data class Symbol(val char: Char, override val x: Int, override val y: Int) : Part(x, y) {
        override fun toString(): String {
            return "$char(${y + 1}:${x + 1})"
        }
    }
}

fun List<String>.toEngineMatrix(): List<List<Part>> {
    val matrix = map {
        it.toCharArray()
    }

    // map engine
    val engine = matrix.map { row ->
        mutableListOf<Part>()
    }

    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            val c = matrix[i][j]
            if (c == '.') {
                continue
            }

            if (c.isDigit()) {
                // if the char to left is a number, continue
                if (j - 1 >= 0 && matrix[i][j - 1].isDigit()) {
                    continue
                }

                // scan right until we find a period or end of array
                var numStr = c.toString()
                var next = j + 1
                while (next < matrix[i].size && matrix[i][next].isDigit()) {
                    numStr = "$numStr${matrix[i][next]}"
                    next++
                }
                // end of number
                engine[i].add(Part.PartNumber(numStr.toInt(), j, i))
                continue
            } else if (c.toString() != ".") {
                engine[i].add(Part.Symbol(c, j, i))
            }
        }
    }
    return engine
}
