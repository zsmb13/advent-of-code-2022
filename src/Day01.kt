import java.util.TreeSet

fun main() {
    fun part1(input: List<String>): Int {
        var max = 0
        var sum = 0

        fun checkAndUpdate() {
            if (sum > max) {
                max = sum
            }
            sum = 0
        }

        for (line in input) {
            if (line.isEmpty()) {
                checkAndUpdate()
                continue
            }
            sum += line.toInt()
        }
        checkAndUpdate()

        return max
    }

    fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> {
        val list = mutableListOf<MutableList<T>>()

        var subList = mutableListOf<T>()
        forEach { element ->
            when {
                predicate(element) -> {
                    list += subList
                    subList = mutableListOf()
                }
                else -> subList += element
            }
        }
        list += subList

        return list
    }

    fun part2(input: List<String>): Int {
        val set = TreeSet<Int>(reverseOrder())

        input.split(String::isEmpty)
            .map { elfInventory -> elfInventory.sumOf(String::toInt) }
            .forEach(set::add)

        return set.take(3).sum()
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
