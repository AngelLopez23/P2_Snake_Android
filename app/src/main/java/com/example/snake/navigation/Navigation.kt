package com.example.snake.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.snake.ui.screens.GameScreen
import com.example.snake.ui.screens.HelpScreen
import com.example.snake.ui.screens.SettingsScreen
import com.example.snake.viewmodel.GameViewModel
import com.example.snake.viewmodel.SettingsViewModel

enum class Screens {
    GAME, SETTINGS, HELP
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AppNavigation(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    gameViewModel: GameViewModel
) {
    NavHost(navController = navController, startDestination = Screens.GAME.name) {
        composable(Screens.GAME.name) {
            GameScreen(gameViewModel, settingsViewModel)
        }
        composable(Screens.SETTINGS.name) {
            SettingsScreen(settingsViewModel)
        }
        composable(Screens.HELP.name) {
            HelpScreen()
        }
    }
}
