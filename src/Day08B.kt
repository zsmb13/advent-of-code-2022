@Suppress("DuplicatedCode")
fun main() {
    fun left(x: Int, y: Int) = (x - 1 downTo 0).asSequence().map { x -> x to y }
    fun right(x: Int, y: Int, xMax: Int) = (x + 1..xMax).asSequence().map { x -> x to y }
    fun top(x: Int, y: Int) = (y - 1 downTo 0).asSequence().map { y -> x to y }
    fun bottom(x: Int, y: Int, yMax: Int) = (y + 1..yMax).asSequence().map { y -> x to y }

    fun part1(testInput: List<String>): Int {
        val heights = testInput.map { line -> line.map(Char::digitToInt) }

        val xValues = testInput.first().indices
        val yValues = testInput.indices

        return yValues.sumOf { y ->
            xValues.count { x ->
                val shorterThanCurrent: (Pair<Int, Int>) -> Boolean =
                        { (nx, ny) -> heights[ny][nx] < heights[y][x] }
                left(x, y).all(shorterThanCurrent) ||
                        top(x, y).all(shorterThanCurrent) ||
                        right(x, y, xValues.last).all(shorterThanCurrent) ||
                        bottom(x, y, yValues.last).all(shorterThanCurrent)
            }
        }
    }

    fun part2(testInput: List<String>): Int {
        val heights = testInput.map { line -> line.map(Char::digitToInt) }

        val xValues = testInput.first().indices
        val yValues = testInput.indices

        return yValues.maxOf { y ->
            xValues.maxOf { x ->
                fun Sequence<Pair<Int, Int>>.viewingDistance(): Int {
                    return when (val blocker = indexOfFirst { (nx, ny) -> heights[ny][nx] >= heights[y][x] }) {
                        -1 -> count()
                        else -> blocker + 1
                    }
                }

                left(x, y).viewingDistance() *
                        top(x, y).viewingDistance() *
                        right(x, y, xValues.last).viewingDistance() *
                        bottom(x, y, yValues.last).viewingDistance()
            }
        }
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
