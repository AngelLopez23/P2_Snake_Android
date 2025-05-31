package com.example.snake.viewmodel

import androidx.lifecycle.ViewModel
import com.example.snake.model.*

class SettingsViewModel : ViewModel() {
    var settings = GameSettings(username = "UnknownUser")
        private set

    fun updateUsername(username: String) {
        settings = settings.copy(username = username)
    }

    fun toggleTimer(enabled: Boolean) {
        settings = settings.copy(timerEnabled = enabled)
    }

    fun setDifficulty(difficulty: Difficulty) {
        settings = settings.copy(difficulty = difficulty)
    }

    fun setFieldSize(fieldSize: FieldSize) {
        settings = settings.copy(fieldSize = fieldSize)
    }
    fun updateRecipientEmail(email: String) {
        settings = settings.copy(recipientEmail = email)
    }
}
