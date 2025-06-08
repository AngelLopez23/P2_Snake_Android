package com.example.snake.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameHistoryEntity::class], version = 1, exportSchema = false)
abstract class GameHistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): GameHistoryDao

    companion object {
        @Volatile private var INSTANCE: GameHistoryDatabase? = null

        fun getDatabase(context: Context): GameHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameHistoryDatabase::class.java,
                    "game_history_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
