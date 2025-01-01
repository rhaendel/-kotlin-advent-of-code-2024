import Direction.NORTH

fun main() {
    val DAY = "Day06"

    println("$DAY part 1")

    fun part1(input: List<String>): Int {
        return Lab(input).doTheGuardWalk().size
    }

    printAndCheck(
        listOf(
            ".#..",
            "...#",
            ".^..",
            "..#."
        ),
        ::part1, 5
    )

    val testInput = readInput("${DAY}_test")
    printAndCheck(testInput, ::part1, 41)

    val input = readInput(DAY)
    printAndCheck(input, ::part1, 4580)
}

private data class Position(val row: Int, val col: Int) {
    fun stepIn(direction: Direction) = Position(row + direction.row, col + direction.col)
}

private enum class Direction(val row: Int, val col: Int) {
    NORTH(-1, 0) {
        override fun turnRight() = EAST
    },
    EAST(0, +1) {
        override fun turnRight() = SOUTH
    },
    SOUTH(+1, 0) {
        override fun turnRight() = WEST
    },
    WEST(0, -1) {
        override fun turnRight() = NORTH
    };

    abstract fun turnRight(): Direction
}

private class Lab(input: List<String>) {

    private val guard = '^'
    private val obstruction = '#'
    private val offMap = ' '

    private val grid = Array(input.size) { CharArray(input[0].length) }

    init {
        input.forEachIndexed { row, line ->
            line.forEachIndexed { col, char -> grid[row][col] = char }
        }
    }

    private fun charAt(position: Position): Char {
        return grid.getOrNull(position.row)?.getOrNull(position.col) ?: offMap
    }

    fun findTheGuard(): Position {
        for ((rowNo, row) in grid.withIndex()) {
            row.indexOf(guard).let { if (it >= 0) return Position(rowNo, it) }
        }
        return Position(-1, -1)
    }

    fun doTheGuardWalk(): Set<Position> {
        val visited = mutableSetOf<Position>()
        var direction = NORTH
        var position = findTheGuard()

        while (charAt(position) != offMap) {
            visited.add(position)
            if (charAt(position.stepIn(direction)) == obstruction) {
                direction = direction.turnRight()
            }
            position = position.stepIn(direction)
        }
        return visited
    }
}
