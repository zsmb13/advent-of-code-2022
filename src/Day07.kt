import java.io.File

fun main() {
    val elfDrive = File("elfdrive")

    fun initializeElfDrive(testInput: List<String>) {
        var file = elfDrive
        file.deleteRecursively()
        file.mkdir()

        testInput.drop(1).forEach { line ->
            when (line.first()) {
                '$' -> {
                    val command = line.split(" ")
                    if (command[1] == "cd") {
                        file = when (val target = command[2]) {
                            ".." -> file.parentFile
                            else -> file.resolve(target)
                        }
                    }
                }
                else -> {
                    val info = line.split(" ")
                    when (info[0]) {
                        "dir" -> file.resolve(info[1]).mkdir()
                        else -> file.resolve(info[1]).writeBytes(ByteArray(info[0].toInt()))
                    }
                }
            }
        }
    }

    fun File.totalSize() = walk().filter(File::isFile).sumOf(File::length)

    fun part1(testInput: List<String>): Long {
        initializeElfDrive(testInput)

        return elfDrive.walk()
            .filter { it.isDirectory }
            .filter { it.totalSize() < 100_000 }
            .sumOf { it.totalSize() }
    }

    fun part2(testInput: List<String>): Long {
        initializeElfDrive(testInput)

        val required = 30_000_000L - (70_000_000L - elfDrive.totalSize())

        return elfDrive.walk()
            .filter(File::isDirectory)
            .filter { it.totalSize() >= required }
            .minOf(File::totalSize)
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95_437L)
    check(part2(testInput) == 24_933_642L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))

    elfDrive.deleteRecursively()
}
