@file:Suppress("DuplicatedCode")

import kotlin.math.abs

fun main() {
    data class Position(val x: Int, val y: Int)

    fun adjacent(p: Position, q: Position) =
        abs(p.x - q.x) <= 1 && abs(p.y - q.y) <= 1

    fun parse(input: List<String>) = input.map { line ->
        line[0] to line.substring(2).toInt()
    }

    fun Char.toDeltaValues() = when (this) {
        'R' -> 1 to 0
        'L' -> -1 to 0
        'U' -> 0 to 1
        'D' -> 0 to -1
        else -> error("Invalid dir $this")
    }

    fun part1(testInput: List<String>): Int {
        var head = Position(0, 0)
        var tail = Position(0, 0)

        val tailPositions = mutableSetOf(tail)

        parse(testInput).forEach { (dir, dist) ->
            val (dx, dy) = dir.toDeltaValues()
            repeat(dist) {
                val prevHead = head
                head = Position(x = head.x + dx, y = head.y + dy)

                if (!adjacent(head, tail)) {
                    // If tail moves, it ends up where head previously was
                    tail = prevHead
                    tailPositions += tail
                }
            }
        }

        return tailPositions.size
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)

    val input = readInput("Day09")
    println(part1(input))
}
