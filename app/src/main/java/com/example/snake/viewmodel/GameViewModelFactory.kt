package com.example.snake.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.snake.data.local.GameHistoryDatabase
import com.example.snake.data.local.GameHistoryRepository
import com.example.snake.service.SoundService

class GameViewModelFactory(
    private val application: Application,
    private val soundService: SoundService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = GameHistoryDatabase.getDatabase(application)
        val repository = GameHistoryRepository(database.historyDao())

        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(application, soundService, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

