@file:Suppress("DuplicatedCode")

import kotlin.math.abs
import kotlin.math.sign

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

    fun Position.follow(other: Position): Position {
        if (adjacent(this, other)) return this

        val dx = (other.x - x).sign
        val dy = (other.y - y).sign
        return Position(x + dx, y + dy)
    }

    fun part1(testInput: List<String>): Int {
        var head = Position(0, 0)
        var tail = Position(0, 0)

        val tailPositions = mutableSetOf(tail)

        parse(testInput).forEach { (dir, dist) ->
            repeat(dist) {
                val (dx, dy) = dir.toDeltaValues()
                head = Position(x = head.x + dx, y = head.y + dy)
                tail = tail.follow(head)
                tailPositions += tail
            }
        }

        return tailPositions.size
    }

    fun part2(testInput: List<String>): Int {
        val snake = Array(10) { Position(0, 0) }
        val tailPositions = mutableSetOf(snake.last())

        parse(testInput).forEach { (dir, dist) ->
            repeat(dist) {
                val (dx, dy) = dir.toDeltaValues()
                snake[0] = Position(x = snake[0].x + dx, y = snake[0].y + dy)
                for (i in 1..9) {
                    snake[i] = snake[i].follow(snake[i - 1])
                }
                tailPositions += snake.last()
            }
        }

        return tailPositions.size
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
