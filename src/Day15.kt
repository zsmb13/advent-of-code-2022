import kotlin.math.abs

fun main() {

    data class Beacon(val x: Int, val y: Int)
    data class Sensor(val x: Int, val y: Int, val beaconDist: Int)

    fun dist(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        return abs(x1 - x2) + abs(y1 - y2)
    }

    fun parse(testInput: List<String>): List<List<Int>> = testInput.map { line ->
        val format = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()
        format.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
    }

    fun part1(testInput: List<String>, xRange: IntRange, yRange: IntRange): Int {
        val input = parse(testInput)
        val sensors = input.map { (x, y, bx, by) ->
            Sensor(x, y, dist(x, y, bx, by))
        }
        val beacons = input.map { (_, _, bx, by) ->
            Beacon(bx, by)
        }

        fun isValid(x: Int, y: Int): Boolean {
            return beacons.any { b -> x == b.x && y == b.y } ||
                sensors.all { s -> dist(s.x, s.y, x, y) > s.beaconDist }
        }

        return xRange.sumOf { x ->
            yRange.count { y ->
                !isValid(x, y)
            }
        }
    }

    /**
     * Generate all points that are just out-of-reach for the given sensor.
     *
     * Shout-out to Marcin Wisniowski (Nohus) for the idea
     * https://kotlinlang.slack.com/archives/C87V9MQFK/p1671087279597559?thread_ts=1671080400.425969&cid=C87V9MQFK
     */
    fun Sensor.pointsOutOfReach(): Sequence<Beacon> {
        val tooFar = beaconDist + 1
        return sequence {
            for (i in 0..tooFar) {
                yield(Beacon(x + i, y - tooFar + i))
                yield(Beacon(x + tooFar - i, y - i))
                yield(Beacon(x - i, y - tooFar + i))
                yield(Beacon(x - tooFar + i, y - i))
            }
        }
    }

    fun part2(testInput: List<String>, xRange: IntRange, yRange: IntRange): Long {
        val sensors = parse(testInput).map { (x, y, bx, by) ->
            Sensor(x, y, dist(x, y, bx, by))
        }

        fun isValid(x: Int, y: Int) = sensors.all { s -> dist(s.x, s.y, x, y) > s.beaconDist }

        sensors.forEach { s ->
            s.pointsOutOfReach()
                .filter { it.x in xRange && it.y in yRange }
                .forEach { (x, y) ->
                    if (isValid(x, y)) {
                        return x.toLong() * 4_000_000 + y.toLong()
                    }
                }
        }
        error("Beacon not found")
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput = testInput, xRange = -50..50, yRange = 10..10)
        .also(::println) == 26)
    check(part2(testInput = testInput, xRange = 0..20, yRange = 0..20)
        .also(::println) == 56000011L)

    val input = readInput("Day15")
    check(part1(testInput = input, xRange = -1_000_000..5_000_000, yRange = 2_000_000..2_000_000)
        .also(::println) == 5100463)
    check(part2(testInput = input, xRange = 0..4_000_000, yRange = 0..4_000_000)
        .also(::println) == 11557863040754L)
}
