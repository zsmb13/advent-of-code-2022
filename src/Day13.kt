package day13

import readInputString

sealed interface PData : Comparable<PData>

data class PInt(val value: Int) : PData {
    override fun toString() = value.toString()

    override fun compareTo(other: PData): Int {
        return when (other) {
            is PInt -> value compareTo other.value
            is PList -> PList(this) compareTo other
        }
    }
}

data class PList(val values: List<PData>) : PData {
    override fun toString() = values.joinToString(separator = ",", prefix = "[", postfix = "]")

    override fun compareTo(other: PData): Int {
        return when (other) {
            is PInt -> this compareTo PList(other)
            is PList -> {
                for (i in values.indices) {
                    if (i !in other.values.indices) {
                        return 1 // Ran out of right values, fail
                    }
                    val result = values[i] compareTo other.values[i]
                    if (result == 0) {
                        continue
                    }
                    return result
                }

                // Right list has more values still, pass
                if (other.values.size > this.values.size) {
                    return -1
                }

                // Identical lists, no result yet
                return 0
            }
        }
    }
}

fun PList(pData: PData) = PList(listOf(pData))

fun parse(s: String): PData {
    if (s.all(Char::isDigit)) return PInt(s.toInt())
    if (s == "[]") return PList(emptyList())

    val csv = s.removeSurrounding("[", "]")
    val values = mutableListOf<PData>()

    var done = 0
    var nesting = 0
    csv.forEachIndexed { i, char ->
        when (char) {
            '[' -> nesting++
            ']' -> nesting--
            ',' -> if (nesting == 0) {
                values += parse(csv.substring(done, i))
                done = i + 1
            }
        }
    }
    values += parse(csv.substring(done))

    return PList(values)
}

fun main() {
    fun part1(testInput: String): Int {
        return testInput.trim()
            .split(System.lineSeparator().repeat(2))
            .mapIndexed { index, it ->
                val (first, second) = it.split(System.lineSeparator()).map(::parse)
                if (first < second) index + 1 else 0
            }
            .sum()
    }

    fun part2(testInput: String): Int {
        val packets = testInput.split(System.lineSeparator())
            .filter(String::isNotBlank)
            .map(::parse)

        val dividers = listOf("[[2]]", "[[6]]").map(::parse)

        val pos1 = packets.count { it < dividers[0] } + 1
        val pos2 = packets.count { it < dividers[1] } + 2

        return pos1 * pos2
    }

    val testInput = readInputString("Day13_test")
    check(part1(testInput).also(::println) == 13)
    check(part2(testInput).also(::println) == 140)

    val input = readInputString("Day13")
    check(part1(input).also(::println) == 5252)
    check(part2(input).also(::println) == 20592)
}
