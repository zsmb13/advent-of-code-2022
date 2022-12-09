fun main() {
    fun part1(testInput: List<String>): Int {
        val map = testInput.map { line ->
            line.map { digit -> digit.digitToInt() }
        }

        val xValues = testInput.first().indices
        val yValues = testInput.indices

        val visible = mutableSetOf<Pair<Int, Int>>()

        // rows
        for (y in yValues) {
            var max = -1
            for (x in xValues) {
                val height = map[y][x]
                if (height > max) {
                    visible += (x to y)
                    max = height
                }
            }
        }
        for (y in yValues) {
            var max = -1
            for (x in xValues.reversed()) {
                val height = map[y][x]
                if (height > max) {
                    visible += (x to y)
                    max = height
                }
            }
        }
        for (x in xValues) {
            var max = -1
            for (y in yValues) {
                val height = map[y][x]
                if (height > max) {
                    visible += (x to y)
                    max = height
                }
            }
        }
        for (x in xValues) {
            var max = -1
            for (y in yValues.reversed()) {
                val height = map[y][x]
                if (height > max) {
                    visible += (x to y)
                    max = height
                }
            }
        }

        return visible.size
    }

    fun part2(testInput: List<String>): Int {
        val xValues = testInput.first().indices
        val yValues = testInput.indices

        var maxScenicScore = 0

        for (y in yValues) {
            for (x in xValues) {
                val height = testInput[y][x]

                val left = testInput[y].substring(0, x)
                    .indexOfLast { it >= height }
                    .let { if (it == -1) x else x - it }

                val right = testInput[y].substring(x + 1)
                    .indexOfFirst { it >= height }
                    .let { if (it == -1) xValues.last - x else it + 1 }

                val top = testInput
                    .take(y)
                    .indexOfLast { it[x] >= height }
                    .let { if (it == -1) y else y - it }

                val bottom = testInput
                    .drop(y + 1)
                    .indexOfFirst { it[x] >= height }
                    .let { if (it == -1) yValues.last - y else it + 1 }

                val score = left * right * top * bottom

                if (score > maxScenicScore) {
                    maxScenicScore = score
                }
            }
        }

        return maxScenicScore
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
