package com.example.snake.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {

    // Insertar un nuevo resultant
    @Insert
    suspend fun insertGameResult(result: GameResult)

    // Obtener todos los resultados
    @Query("SELECT * FROM game_results ORDER BY timestamp DESC")
    suspend fun getAllGameResults(): List<GameResult>
}
