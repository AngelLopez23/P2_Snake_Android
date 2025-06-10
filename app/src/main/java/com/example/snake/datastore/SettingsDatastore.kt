package com.example.snake.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.snake.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        private val USERNAME = stringPreferencesKey("username")
        private val EMAIL = stringPreferencesKey("email")
        private val TIMER_ENABLED = booleanPreferencesKey("timer_enabled")
        private val DIFFICULTY = stringPreferencesKey("difficulty")
        private val FIELD_SIZE = stringPreferencesKey("field_size")
    }

    val settingsFlow: Flow<GameSettings> = context.dataStore.data.map { preferences ->
        GameSettings(
            username = preferences[USERNAME] ?: "Default",
            recipientEmail = preferences[EMAIL] ?: "Default",
            timerEnabled = preferences[TIMER_ENABLED] ?: false,
            difficulty = Difficulty.valueOf(preferences[DIFFICULTY] ?: Difficulty.MEDIUM.name),
            fieldSize = FieldSize.valueOf(preferences[FIELD_SIZE] ?: FieldSize.MEDIUM.name)
        )
    }

    suspend fun saveSettings(settings: GameSettings) {
        context.dataStore.edit { prefs ->
            prefs[USERNAME] = settings.username
            prefs[EMAIL] = settings.recipientEmail
            prefs[TIMER_ENABLED] = settings.timerEnabled
            prefs[DIFFICULTY] = settings.difficulty.name
            prefs[FIELD_SIZE] = settings.fieldSize.name
        }
    }
}
