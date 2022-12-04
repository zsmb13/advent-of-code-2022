fun main() {

    fun parse(input: List<String>) = input
        .map { line ->
            val (a1, a2) = line.substringBefore(',').split('-').map(String::toInt)
            val (b1, b2) = line.substringAfter(',').split('-').map(String::toInt)
            (a1..a2) to (b1..b2)
        }

    operator fun IntRange.contains(other: IntRange) =
        other.first in this && other.last in this

    fun part1(input: List<String>) = parse(input)
        .count { (r1, r2) -> r1 in r2 || r2 in r1 }

    fun part2(input: List<String>) = parse(input)
        .count { (r1, r2) -> r1.intersect(r2).isNotEmpty() }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
