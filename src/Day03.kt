fun main() {

    fun Char.toPriority(): Int {
        return when (this) {
            in 'a'..'z' -> this - 'a' + 1
            in 'A'..'Z' -> this - 'A' + 27
            else -> error("Invalid char $this")
        }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val first = line.substring(0, line.length / 2)
            val second = line.substring(line.length / 2)

            first.toSet()
                .intersect(second.toSet())
                .first()
                .toPriority()
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3)
            .sumOf { (elf1, elf2, elf3) ->
                elf1.toSet()
                    .intersect(elf2.toSet())
                    .intersect(elf3.toSet())
                    .first()
                    .toPriority()
            }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
