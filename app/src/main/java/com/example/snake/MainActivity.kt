package com.example.snake

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.snake.navigation.AppNavigation
import com.example.snake.navigation.Screens
import com.example.snake.ui.theme.SnakeGameTheme
import com.example.snake.viewmodel.GameViewModel
import com.example.snake.viewmodel.SettingsViewModel
import androidx.compose.ui.Modifier
import com.example.snake.service.SoundService
import com.example.snake.viewmodel.GameViewModelFactory

class MainActivity : ComponentActivity() {
    private var soundService: SoundService? = null
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnakeGameTheme {
                val navController = rememberNavController()
                val settingsViewModel: SettingsViewModel = viewModel()

                val localService = remember {
                    SoundService(applicationContext)
                }.also {
                    soundService = it
                }
                val gameViewModel: GameViewModel = viewModel(
                    factory = GameViewModelFactory(application, localService)
                )


                var selectedScreen by remember { mutableStateOf(Screens.GAME) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Snake Game") },
                            actions = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Salir")
                                }
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = selectedScreen == Screens.HELP,
                                onClick = {
                                    selectedScreen = Screens.HELP
                                    navController.navigate(Screens.HELP.name)
                                },
                                icon = { Icon(Icons.Default.Info, contentDescription = "Ayuda") },
                                label = { Text("Ayuda") }
                            )
                            NavigationBarItem(
                                selected = selectedScreen == Screens.GAME,
                                onClick = {
                                    selectedScreen = Screens.GAME
                                    navController.navigate(Screens.GAME.name)
                                },
                                icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Jugar") },
                                label = { Text("Jugar") }
                            )
                            NavigationBarItem(
                                selected = selectedScreen == Screens.SETTINGS,
                                onClick = {
                                    selectedScreen = Screens.SETTINGS
                                    navController.navigate(Screens.SETTINGS.name)
                                },
                                icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
                                label = { Text("Ajustes") }
                            )
                        }
                    }
                ) { padding ->
                    Surface(modifier = Modifier.padding(padding)) {
                        AppNavigation(navController, settingsViewModel, gameViewModel)
                    }
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        soundService?.releaseAll()
    }

}
