package com.example.snake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.snake.data.AppDatabase
import com.example.snake.data.GameResult
import java.lang.reflect.Modifier
import androidx.compose.foundation.layout.*  // Importa todo lo relacionado con el layout (como Modifier, Spacer, etc.)
import androidx.compose.material3.*  // Para las funcionalidades de Material3 como Card, Text, etc.
import androidx.compose.ui.unit.dp  // Para trabajar con dimensiones (dp)
import androidx.compose.runtime.*  // Para el uso de remember y LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview  // Si quieres hacer pruebas con un Preview

@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val gameResultDao = db.gameResultDao()

    // State para almacenar los resultados
    val gameResults = remember { mutableStateOf<List<GameResult>>(emptyList()) }

    // Cargar los resultados desde la base de datos
    LaunchedEffect(Unit) {
        // Obtener los resultados almacenados en la base de datos
        gameResults.value = gameResultDao.getAllGameResults()
    }

    Column() {
        Text("Historial de partidas", style = MaterialTheme.typography.headlineSmall)

        // Mostrar los resultados en LazyColumn
        LazyColumn {
            items(gameResults.value) { result ->
                GameResultItem(result) // Composable para mostrar cada resultado
            }
        }
    }
}

@Composable
fun GameResultItem(result: GameResult) {
    Card() {
        Column {
            Text("Jugador: ${result.username}", style = MaterialTheme.typography.bodyMedium)
            Text("Correo: ${result.recipientEmail}", style = MaterialTheme.typography.bodySmall)
            Text("Puntuación: ${result.score}", style = MaterialTheme.typography.bodySmall)
            Text("Estado: ${if (result.isGameWon) "Victoria" else "Derrota"}", style = MaterialTheme.typography.bodySmall)
            Text("Tiempo restante: ${result.remainingTime} segundos", style = MaterialTheme.typography.bodySmall)
            Text("Fecha: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(result.timestamp)}", style = MaterialTheme.typography.bodySmall)
        }
    }
}