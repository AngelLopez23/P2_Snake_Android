package com.example.snake.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // ID autogenerado
    val username: String,
    val recipientEmail: String,
    val score: Int,
    val isGameWon: Boolean,
    val remainingTime: Int,
    val timestamp: Long = System.currentTimeMillis(),  // Marca temporal para cuando se guarda el resultado
    val logContent: String
)
