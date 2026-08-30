package com.example.mazegame

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.abs

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Maze Game",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Moves: ${viewModel.moveCount}",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            MazeCanvas(viewModel)
        }

        Controls(viewModel)
        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.hasWon) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    TextButton(onClick = { viewModel.resetGame() }) {
                        Text("Play Again")
                    }
                },
                title = { Text("You reached the exit!") },
                text = { Text("Solved in ${viewModel.moveCount} moves.") }
            )
        }
    }
}

@Composable
fun MazeCanvas(viewModel: GameViewModel) {
    val rows = viewModel.rows
    val cols = viewModel.cols
    val maze = viewModel.maze

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                var dragX = 0f
                var dragY = 0f
                detectDragGestures(
                    onDragStart = { dragX = 0f; dragY = 0f },
                    onDragEnd = {
                        if (abs(dragX) > abs(dragY)) {
                            if (dragX > 0) viewModel.move(Direction.RIGHT)
                            else viewModel.move(Direction.LEFT)
                        } else {
                            if (dragY > 0) viewModel.move(Direction.DOWN)
                            else viewModel.move(Direction.UP)
                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    dragX += dragAmount.x
                    dragY += dragAmount.y
                }
            }
    ) {
        val cellSize = minOf(size.width / cols, size.height / rows)
        val offsetX = (size.width - cellSize * cols) / 2f
        val offsetY = (size.height - cellSize * rows) / 2f
        val wallColor = Color(0xFFE94560)
        val strokeWidth = 6f

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val cell = maze[r][c]
                val x = offsetX + c * cellSize
                val y = offsetY + r * cellSize

                if (cell.top) drawLine(wallColor, Offset(x, y), Offset(x + cellSize, y), strokeWidth, cap = StrokeCap.Round)
                if (cell.left) drawLine(wallColor, Offset(x, y), Offset(x, y + cellSize), strokeWidth, cap = StrokeCap.Round)
                if (cell.right) drawLine(wallColor, Offset(x + cellSize, y), Offset(x + cellSize, y + cellSize), strokeWidth, cap = StrokeCap.Round)
                if (cell.bottom) drawLine(wallColor, Offset(x, y + cellSize), Offset(x + cellSize, y + cellSize), strokeWidth, cap = StrokeCap.Round)
            }
        }

        // Exit marker (green)
        val exitX = offsetX + viewModel.exitCol * cellSize + cellSize / 2f
        val exitY = offsetY + viewModel.exitRow * cellSize + cellSize / 2f
        drawCircle(Color(0xFF16C79A), radius = cellSize / 3f, center = Offset(exitX, exitY))

        // Player marker (yellow)
        val playerX = offsetX + viewModel.playerCol * cellSize + cellSize / 2f
        val playerY = offsetY + viewModel.playerRow * cellSize + cellSize / 2f
        drawCircle(Color(0xFFFFD460), radius = cellSize / 3f, center = Offset(playerX, playerY))
    }
}

@Composable
fun Controls(viewModel: GameViewModel) {
    val buttonColors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F3460))
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { viewModel.move(Direction.UP) }, colors = buttonColors) { Text("▲") }
        Row {
            Button(onClick = { viewModel.move(Direction.LEFT) }, colors = buttonColors) { Text("◀") }
            Spacer(modifier = Modifier.width(48.dp))
            Button(onClick = { viewModel.move(Direction.RIGHT) }, colors = buttonColors) { Text("▶") }
        }
        Button(onClick = { viewModel.move(Direction.DOWN) }, colors = buttonColors) { Text("▼") }
    }
}
