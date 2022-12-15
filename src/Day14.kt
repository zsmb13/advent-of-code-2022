data class Position(val x: Int, val y: Int)

fun Position.below() = Position(x, y + 1)
fun Position.left() = Position(x - 1, y + 1)
fun Position.right() = Position(x + 1, y + 1)

fun createOccupiedSet(paths: List<List<Position>>): MutableSet<Position> {
    return buildSet {
        paths.forEach { path ->
            path.zipWithNext().forEach { (p1, p2) ->
                val (x1, y1) = p1
                val (x2, y2) = p2

                (minOf(y1, y2)..maxOf(y1, y2)).forEach { y ->
                    (minOf(x1, x2)..maxOf(x1, x2)).forEach { x ->
                        add(Position(x, y))
                    }
                }
            }
        }
    }.toMutableSet()
}

fun Position.computeNext(occupied: MutableSet<Position>) = sequence {
    yield(below())
    yield(left())
    yield(right())
}.firstOrNull { it !in occupied }

inline fun runSimulation(
    testInput: List<String>,
    drop: (MutableSet<Position>, Int, Position, Position?) -> Position?
): Int {
    val paths: List<List<Position>> = testInput.map { line ->
        line.split(" -> ").map { coordinates ->
            val (x, y) = coordinates.split(',').map(String::toInt)
            Position(x, y)
        }
    }
    val yMax = paths.maxOf { path -> path.maxOf(Position::y) }

    val occupied = createOccupiedSet(paths)
    val initialSize = occupied.size

    runCatching {
        while (true) {
            var s = Position(500, 0)
            while (true) {
                val proposedNext = s.computeNext(occupied)
                val next = drop(occupied, yMax, s, proposedNext)
                if (next == null) {
                    occupied += s
                    break
                }
                s = next
            }
        }
    }

    return occupied.size - initialSize
}

fun main() {
    fun part1(testInput: List<String>): Int {
        return runSimulation(testInput) { _, yMax, s, next ->
            // Run until sand doesn't fall past yMax
            check(s.y <= yMax)
            next
        }
    }

    fun part2(testInput: List<String>): Int {
        return runSimulation(testInput) { occupied, yMax, s, next ->
            // Hit the floor - override next value
            if (s.y == yMax + 1) {
                occupied += s
                return@runSimulation null
            }

            // Run until we're NOT stuck in the starting position
            check((next == null && s == Position(500, 0)).not()) {
                // Mark starting position occupied before wrapping up
                occupied.add(s)
            }
            next
        }
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput).also(::println) == 24)
    check(part2(testInput).also(::println) == 93)

    val input = readInput("Day14")
    check(part1(input).also(::println) == 692)
    check(part2(input).also(::println) == 31706)
}
