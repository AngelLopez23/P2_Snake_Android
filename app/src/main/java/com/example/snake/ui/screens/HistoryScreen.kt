package com.example.snake.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.snake.data.local.GameHistoryEntity
import com.example.snake.model.GameHistoryEntry
import com.example.snake.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel) {
    val history by historyViewModel.history.collectAsState()

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape && history.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            history.chunked((history.size + 1) / 2).forEach { sublist ->
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sublist.forEach { entry ->
                        HistoryCard(entry)
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (history.isEmpty()) {
                Text(
                    text = "Encara no hi ha partides guardades.",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                history.forEach { entry ->
                    HistoryCard(entry)
                }
            }
        }
    }

}

@Composable
fun HistoryCard(entry: GameHistoryEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Usuari: ${entry.username}", style = MaterialTheme.typography.titleMedium)
            Text("Punts: ${entry.score}")
            Text("Guanyada: ${if (entry.isGameWon) "Sí" else "No"}")
            Text("Data: ${entry.timestamp}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Log:", style = MaterialTheme.typography.labelMedium)
            Text(entry.log, style = MaterialTheme.typography.bodySmall)
        }
    }
}
