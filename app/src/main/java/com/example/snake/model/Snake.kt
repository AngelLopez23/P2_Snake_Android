package com.example.snake.model

import com.example.snake.model.GridPosition

data class Snake(
    val body: List<GridPosition>,
    val direction: Direction
)