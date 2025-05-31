package com.example.snake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.snake.model.*
import com.example.snake.viewmodel.SettingsViewModel
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import androidx.compose.ui.res.stringResource
import com.example.snake.R

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    var username by remember { mutableStateOf(settingsViewModel.settings.username) }
    var recipientEmail by remember { mutableStateOf(settingsViewModel.settings.recipientEmail) }
    var timerEnabled by remember { mutableStateOf(settingsViewModel.settings.timerEnabled) }
    var selectedDifficulty by remember { mutableStateOf(settingsViewModel.settings.difficulty) }
    var selectedFieldSize by remember { mutableStateOf(settingsViewModel.settings.fieldSize) }
    val orientation = LocalConfiguration.current.orientation
    val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        settingsViewModel.updateUsername(it)
                    },
                    label = { Text(stringResource(id = R.string.settings_username_label)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = recipientEmail,
                    onValueChange = {
                        recipientEmail = it
                        settingsViewModel.updateRecipientEmail(it)
                    },
                    label = { Text(stringResource(R.string.settings_email_label)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = timerEnabled,
                        onCheckedChange = {
                            timerEnabled = it
                            settingsViewModel.toggleTimer(it)
                        }
                    )
                    Text(stringResource(id = R.string.settings_timer_label))
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(id = R.string.settings_difficulty_label))
                Difficulty.values().forEach { difficulty ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedDifficulty == difficulty,
                            onClick = {
                                selectedDifficulty = difficulty
                                settingsViewModel.setDifficulty(difficulty)
                            }
                        )
                        val difficultyLabel = when (difficulty) {
                            Difficulty.EASY -> R.string.difficulty_easy
                            Difficulty.MEDIUM -> R.string.difficulty_medium
                            Difficulty.HARD -> R.string.difficulty_hard
                        }
                        Text(stringResource(id = difficultyLabel))
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(id = R.string.settings_field_size_label))
                FieldSize.values().forEach { size ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedFieldSize == size,
                            onClick = {
                                selectedFieldSize = size
                                settingsViewModel.setFieldSize(size)
                            }
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
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    settingsViewModel.updateUsername(it)
                },
                label = { Text(stringResource(id = R.string.settings_username_label)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = recipientEmail,
                onValueChange = {
                    recipientEmail = it
                    settingsViewModel.updateRecipientEmail(it)
                },
                label = { Text(stringResource(R.string.settings_email_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = timerEnabled,
                    onCheckedChange = {
                        timerEnabled = it
                        settingsViewModel.toggleTimer(it)
                    }
                )
                Text(stringResource(id = R.string.settings_timer_label))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(id = R.string.settings_difficulty_label))
            Difficulty.values().forEach { difficulty ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedDifficulty == difficulty,
                        onClick = {
                            selectedDifficulty = difficulty
                            settingsViewModel.setDifficulty(difficulty)
                        }
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
                        selected = selectedFieldSize == size,
                        onClick = {
                            selectedFieldSize = size
                            settingsViewModel.setFieldSize(size)
                        }
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
    }
}
