package com.example.mazegame

import kotlin.random.Random

/**
 * A single cell in the maze grid. Each side is `true` if a wall is present.
 */
data class Cell(
    var top: Boolean = true,
    var right: Boolean = true,
    var bottom: Boolean = true,
    var left: Boolean = true,
    var visited: Boolean = false
)

/**
 * Generates a perfect maze (no loops, every cell reachable) using the
 * recursive backtracker algorithm.
 */
class MazeGenerator(private val rows: Int, private val cols: Int) {

    private val grid = Array(rows) { Array(cols) { Cell() } }

    fun generate(): Array<Array<Cell>> {
        val stack = ArrayDeque<Pair<Int, Int>>()
        var current = 0 to 0
        grid[0][0].visited = true
        stack.addLast(current)

        while (stack.isNotEmpty()) {
            val (r, c) = current
            val neighbors = unvisitedNeighbors(r, c)

            if (neighbors.isNotEmpty()) {
                val (nr, nc, dir) = neighbors[Random.nextInt(neighbors.size)]
                removeWallBetween(r, c, nr, nc, dir)
                grid[nr][nc].visited = true
                current = nr to nc
                stack.addLast(current)
            } else {
                stack.removeLast()
                if (stack.isNotEmpty()) current = stack.last()
            }
        }
        return grid
    }

    private fun unvisitedNeighbors(r: Int, c: Int): List<Triple<Int, Int, String>> {
        val list = mutableListOf<Triple<Int, Int, String>>()
        if (r > 0 && !grid[r - 1][c].visited) list.add(Triple(r - 1, c, "top"))
        if (r < rows - 1 && !grid[r + 1][c].visited) list.add(Triple(r + 1, c, "bottom"))
        if (c > 0 && !grid[r][c - 1].visited) list.add(Triple(r, c - 1, "left"))
        if (c < cols - 1 && !grid[r][c + 1].visited) list.add(Triple(r, c + 1, "right"))
        return list
    }

    private fun removeWallBetween(r: Int, c: Int, nr: Int, nc: Int, dir: String) {
        when (dir) {
            "top" -> { grid[r][c].top = false; grid[nr][nc].bottom = false }
            "bottom" -> { grid[r][c].bottom = false; grid[nr][nc].top = false }
            "left" -> { grid[r][c].left = false; grid[nr][nc].right = false }
            "right" -> { grid[r][c].right = false; grid[nr][nc].left = false }
        }
    }
}
