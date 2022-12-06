fun main() {
    fun String.allDistinct() = toSet().size == length

    fun findUniqueMarker(str: String, n: Int) =
        str.windowed(n).indexOfFirst(String::allDistinct) + n
    //  str.windowed(n).takeWhile { lastN -> lastN.toSet().size != n }.size + n

    fun part1(testInput: String) = findUniqueMarker(testInput, 4)
    fun part2(testInput: String) = findUniqueMarker(testInput, 14)

    val testInput = readInput("Day06_test").first()
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06").first()
    println(part1(input))
    println(part2(input))
}
