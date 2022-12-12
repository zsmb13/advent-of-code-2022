package day11

import readInput

class Monkey(
    val items: MutableList<Long>,
    val op: (Long) -> Long,
    val divisor: Long,
    val targets: Pair<Int, Int>,
) {
    var inspections = 0L

    fun performRound(calm: (Long) -> Long) {
        while (true) {
            val item = items.removeFirstOrNull() ?: return

            val newItem = calm(op(item)).also { inspections++ }
            throwItemTo(
                target = if (newItem % divisor == 0L) targets.first else targets.second,
                item = newItem,
            )
        }
    }

    companion object {
        fun parse(data: List<String>): Monkey {
            val (symbol, value) = data[2].substringAfter("new = old ").split(" ")

            return Monkey(
                items = data[1].substringAfter("items: ")
                    .split(", ")
                    .map(String::toLong)
                    .toMutableList(),
                op = when (symbol) {
                    "*" -> {
                        if (value == "old") { old -> old * old }
                        else { old -> old * value.toLong() }
                    }
                    "+" -> { { it + value.toLong() } }
                    else -> error("Unexpected operation")
                },
                divisor = data[3].substringAfter("by ").toLong(),
                targets = data[4].substringAfterLast(" ").toInt() to data[5].substringAfterLast(" ").toInt(),
            )
        }
    }
}

var monkeys = listOf<Monkey>()

fun throwItemTo(target: Int, item: Long) {
    monkeys[target].items += item
}

val List<Monkey>.monkeyBusiness
    get() = sortedByDescending(Monkey::inspections)
        .take(2)
        .let { (m1, m2) -> m1.inspections * m2.inspections }

fun main() {
    fun runMonkeys(
        testInput: List<String>,
        iterations: Int,
        calming: (Long) -> Long
    ): Long {
        monkeys = testInput.chunked(7).map(Monkey::parse)

        repeat(iterations) {
            monkeys.forEach { monkey ->
                monkey.performRound(calm = calming)
            }
        }

        return monkeys.monkeyBusiness
    }

    fun part1(testInput: List<String>) = runMonkeys(testInput = testInput, iterations = 20, calming = { it / 3 })

    fun part2(testInput: List<String>): Long {
        val calmingNumber = monkeys.fold(1L) { acc, monkey -> acc * monkey.divisor }
        return runMonkeys(testInput = testInput, iterations = 10_000, calming = { it % calmingNumber })
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10_605L)
    check(part2(testInput) == 2_713_310_158L)

    val input = readInput("Day11")
    check(part1(input).also(::println) == 56_595L)
    check(part2(input).also(::println) == 15_693_274_740L)
}
