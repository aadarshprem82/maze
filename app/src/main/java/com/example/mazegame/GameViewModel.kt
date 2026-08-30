package com.example.mazegame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class Direction { UP, DOWN, LEFT, RIGHT }

class GameViewModel : ViewModel() {

    val rows = 15
    val cols = 10

    var maze = MazeGenerator(rows, cols).generate()
        private set

    var playerRow by mutableStateOf(0)
        private set
    var playerCol by mutableStateOf(0)
        private set

    var moveCount by mutableStateOf(0)
        private set

    var hasWon by mutableStateOf(false)
        private set

    val exitRow = rows - 1
    val exitCol = cols - 1

    fun move(direction: Direction) {
        if (hasWon) return
        val cell = maze[playerRow][playerCol]
        var newRow = playerRow
        var newCol = playerCol

        when (direction) {
            Direction.UP -> if (!cell.top) newRow -= 1
            Direction.DOWN -> if (!cell.bottom) newRow += 1
            Direction.LEFT -> if (!cell.left) newCol -= 1
            Direction.RIGHT -> if (!cell.right) newCol += 1
        }

        if (newRow != playerRow || newCol != playerCol) {
            playerRow = newRow
            playerCol = newCol
            moveCount++
            if (playerRow == exitRow && playerCol == exitCol) {
                hasWon = true
            }
        }
    }

    fun resetGame() {
        maze = MazeGenerator(rows, cols).generate()
        playerRow = 0
        playerCol = 0
        moveCount = 0
        hasWon = false
    }
}
