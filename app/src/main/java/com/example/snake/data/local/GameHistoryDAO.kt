package com.example.snake.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDao {
    @Insert
    suspend fun insertHistory(entry: GameHistoryEntity)

    @Query("SELECT * FROM game_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<GameHistoryEntity>>
}
