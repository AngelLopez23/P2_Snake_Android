package com.example.snake.model

data class GameHistoryEntry(
    val username: String,
    val score: Int,
    val isGameWon: Boolean,
    val timestamp: String,
    val log: String
)
