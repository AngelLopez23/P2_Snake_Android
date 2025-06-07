package com.example.snake.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.snake.R
import com.example.snake.model.*
import com.example.snake.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val settings by settingsViewModel.settings.collectAsState()

    val orientation = LocalConfiguration.current.orientation
    val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

    val content: @Composable ColumnScope.() -> Unit = {
        OutlinedTextField(
            value = settings.username,
            onValueChange = { settingsViewModel.updateUsername(it) },
            label = { Text(stringResource(id = R.string.settings_username_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = settings.recipientEmail,
            onValueChange = { settingsViewModel.updateRecipientEmail(it) },
            label = { Text(stringResource(R.string.settings_email_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = settings.timerEnabled,
                onCheckedChange = { settingsViewModel.toggleTimer(it) }
            )
            Text(stringResource(id = R.string.settings_timer_label))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(id = R.string.settings_difficulty_label))
        Difficulty.values().forEach { difficulty ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = settings.difficulty == difficulty,
                    onClick = { settingsViewModel.setDifficulty(difficulty) }
                )
                val difficultyLabel = when (difficulty) {
                    Difficulty.EASY -> R.string.difficulty_easy
                    Difficulty.MEDIUM -> R.string.difficulty_medium
                    Difficulty.HARD -> R.string.difficulty_hard
                }
                Text(stringResource(id = difficultyLabel))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(id = R.string.settings_field_size_label))
        FieldSize.values().forEach { size ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = settings.fieldSize == size,
                    onClick = { settingsViewModel.setFieldSize(size) }
                )
                val sizeLabel = when (size) {
                    FieldSize.SMALL -> R.string.field_small
                    FieldSize.MEDIUM -> R.string.field_medium
                    FieldSize.LARGE -> R.string.field_large
                }
                Text(stringResource(id = sizeLabel))
            }
        }
    }

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) { content() }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            content()
        }
    }
}
