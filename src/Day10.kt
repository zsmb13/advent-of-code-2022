fun main() {
    fun getXValues(testInput: List<String>) = testInput
        .flatMap { line ->
            when (line) {
                "noop" -> listOf(0)
                else -> listOf(0, line.substring(5).toInt())
            }
        }.runningFold(1) { x, op -> x + op }

    fun part1(testInput: List<String>): Int {
        val xValues = getXValues(testInput)
        val cyclesOfInterest = listOf(20, 60, 100, 140, 180, 220)

        return cyclesOfInterest.sumOf { index ->
            index * xValues[index - 1]
        }
    }

    fun part2(testInput: List<String>) {
        (1..240).zip(getXValues(testInput))
            .forEach { (cycle, x) ->
                val sprite = x until (x + 3)
                val pos = cycle % 40
                print(if (pos in sprite) '#' else '.')

                if (pos == 0) println()
            }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    part2(testInput)

    val input = readInput("Day10")
    println(part1(input))
    part2(input)
}
