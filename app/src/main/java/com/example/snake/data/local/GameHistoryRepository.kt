package com.example.snake.data.local

import kotlinx.coroutines.flow.Flow

class GameHistoryRepository(private val dao: GameHistoryDao) {

    fun getAllHistory(): Flow<List<GameHistoryEntity>> = dao.getAllHistory()

    suspend fun save(entry: GameHistoryEntity) {
        dao.insertHistory(entry)
    }
}
