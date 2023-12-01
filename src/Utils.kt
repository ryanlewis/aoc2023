import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Converts string to list of numbers. Considers worded numbers as well (.e.g. "one" -> 1).
 *
 * Note: eightwo -> [8, 2]
 */
fun String.toNumberList(): List<Int> {
    val m = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )
    val numbers = mutableListOf<Int>()
    for (i in indices) {
        val part = this.substring(i)

        if (part.first().isDigit()) {
            numbers.add(part.first().digitToInt())
            continue
        }

        val key = m.keys.find { part.startsWith(it) }
        if (key != null) {
            numbers.add(m[key]!!)
        }
    }

    return numbers
}
