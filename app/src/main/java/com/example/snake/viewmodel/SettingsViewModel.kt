package com.example.snake.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snake.datastore.SettingsRepository
import com.example.snake.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    private val _settings = MutableStateFlow(GameSettings())
    val settings: StateFlow<GameSettings> = _settings

    init {
        viewModelScope.launch {
            repository.settings.collect {
                _settings.value = it
            }
        }
    }

    fun updateUsername(username: String) {
        _settings.update { it.copy(username = username) }
        persist()
    }

    fun toggleTimer(enabled: Boolean) {
        _settings.update { it.copy(timerEnabled = enabled) }
        persist()
    }

    fun setDifficulty(difficulty: Difficulty) {
        _settings.update { it.copy(difficulty = difficulty) }
        persist()
    }

    fun setFieldSize(fieldSize: FieldSize) {
        _settings.update { it.copy(fieldSize = fieldSize) }
        persist()
    }

    fun updateRecipientEmail(email: String) {
        _settings.update { it.copy(recipientEmail = email) }
        persist()
    }

    private fun persist() {
        viewModelScope.launch {
            repository.updateSettings(_settings.value)
        }
    }
}
