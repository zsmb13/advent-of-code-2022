fun main() {

    data class Move(val count: Int, val from: Int, val to: Int)

    fun parse(testInput: List<String>): Pair<List<MutableList<Char>>, List<Move>> {
        val blankIndex = testInput.indexOfFirst(String::isBlank)

        val crateCount = testInput[blankIndex - 1]
            .last(Char::isDigit)
            .digitToInt()
        val crates = List(size = crateCount) { mutableListOf<Char>() }
        testInput.subList(0, blankIndex - 1).asReversed().forEach { line ->
            line.chunked(4).forEachIndexed { index, content ->
                content[1].takeIf(Char::isLetter)?.let(crates[index]::plusAssign)
            }
        }

        val moves = testInput.subList(blankIndex + 1, testInput.size)
            .map {
                val split = it.split(" ")
                Move(split[1].toInt(), split[3].toInt() - 1, split[5].toInt() - 1)
            }

        return Pair(crates, moves)
    }

    fun topsOf(crates: List<MutableList<Char>>) =
        crates.map(List<Char>::last).joinToString(separator = "")

    fun part1(testInput: List<String>): String {
        val (crates, moves) = parse(testInput)

        moves.forEach { move ->
            repeat(move.count) {
                crates[move.to] += crates[move.from].removeLast()
            }
        }

        return topsOf(crates)
    }

    fun <T> MutableList<T>.removeLast(count: Int): List<T> {
        val removeIndex = this.size - count
        return List(size = count) { this.removeAt(removeIndex) }
    }

    fun part2(testInput: List<String>): String {
        val (crates, moves) = parse(testInput)

        moves.forEach { move ->
            crates[move.to] += crates[move.from].removeLast(move.count)
        }

        return topsOf(crates)
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
