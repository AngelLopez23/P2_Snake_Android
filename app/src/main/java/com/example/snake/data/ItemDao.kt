package com.example.snake.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    // Insertar un nuevo resultant
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItems(result: Item)

    // Obtener todos los resultados
    @Query("SELECT * FROM Items ORDER BY timestamp DESC")
    suspend fun getAllItems(): Flow<List<Item>>

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)
}
