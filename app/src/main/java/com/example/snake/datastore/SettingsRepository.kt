package com.example.snake.datastore

import com.example.snake.model.GameSettings
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val dataStore: SettingsDataStore) {

    val settings: Flow<GameSettings> = dataStore.settingsFlow

    suspend fun updateSettings(settings: GameSettings) {
        dataStore.saveSettings(settings)
    }
}
