package com.example.snake.model

data class GameState(
    val snake: Snake,
    val food: GridPosition,
    val score: Int = 0,
    val isGameOver: Boolean = false,
    val isGameWon: Boolean = false
)