package com.example.snake.model

data class GameSettings(
    val username: String = "",
    val timerEnabled: Boolean = false,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val fieldSize: FieldSize = FieldSize.MEDIUM,
    val recipientEmail: String = ""
)

enum class Difficulty(val speed: Long) {
    EASY(500L),
    MEDIUM(300L),
    HARD(150L)
}

enum class FieldSize(val width: Int, val height: Int) {
    SMALL(10, 10),
    MEDIUM(15, 15),
    LARGE(20, 20)
}