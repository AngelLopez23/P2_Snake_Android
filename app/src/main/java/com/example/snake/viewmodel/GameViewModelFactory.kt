package com.example.snake.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.snake.service.SoundService

class GameViewModelFactory(
    private val application: Application,
    private val soundService: SoundService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(application, soundService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
