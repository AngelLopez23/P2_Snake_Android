package com.example.snake.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.snake.data.local.GameHistoryDatabase
import com.example.snake.data.local.GameHistoryRepository
import com.example.snake.model.GameHistoryEntry
import com.example.snake.ui.screens.GameScreen
import com.example.snake.ui.screens.HelpScreen
import com.example.snake.ui.screens.HistoryScreen
import com.example.snake.ui.screens.SettingsScreen
import com.example.snake.viewmodel.GameViewModel
import com.example.snake.viewmodel.HistoryViewModel
import com.example.snake.viewmodel.HistoryViewModelFactory
import com.example.snake.viewmodel.SettingsViewModel

enum class Screens {
    GAME, SETTINGS, HELP, HISTORY
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
        composable(Screens.HISTORY.name) {
            val context = LocalContext.current
            val db = remember { GameHistoryDatabase.getDatabase(context) }
            val repo = remember { GameHistoryRepository(db.historyDao()) }
            val viewModel: HistoryViewModel = viewModel(factory = HistoryViewModelFactory(repo))
            HistoryScreen(viewModel)
        }
    }
}
