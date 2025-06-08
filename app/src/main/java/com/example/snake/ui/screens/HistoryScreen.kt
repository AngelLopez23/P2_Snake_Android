package com.example.snake.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    val orientation = LocalConfiguration.current.orientation
    val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE
    var selectedEntry by remember { mutableStateOf<GameHistoryEntity?>(null) }

    if(isLandscape){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 📋 Panell esquerre: resum de partides
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(history) { entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedEntry = entry },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Usuari: ${entry.username}", style = MaterialTheme.typography.titleSmall)
                            Text("Punts: ${entry.score}")
                            Text("Guanyada: ${if (entry.isGameWon) "Sí" else "No"}")
                            Text("Data: ${entry.timestamp}")
                        }
                    }
                }
            }

            // 📄 Panell dret: detall complet amb targeta
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {
                if (selectedEntry != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text("Detall complet", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Usuari: ${selectedEntry!!.username}")
                            Text("Punts: ${selectedEntry!!.score}")
                            Text("Guanyada: ${if (selectedEntry!!.isGameWon) "Sí" else "No"}")
                            Text("Data: ${selectedEntry!!.timestamp}")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Log de la partida:", style = MaterialTheme.typography.labelLarge)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(selectedEntry!!.log, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                } else {
                    Text(
                        "Selecciona una partida per veure els detalls.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }else{//portrait
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Panell superior: Resums
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(history) { entry ->
                    Card(
                        modifier = Modifier
                            .width(200.dp)
                            .clickable { selectedEntry = entry },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Usuari: ${entry.username}", style = MaterialTheme.typography.titleSmall)
                            Text("Punts: ${entry.score}")
                            Text("Guanyada: ${if (entry.isGameWon) "Sí" else "No"}")
                            Text("Data: ${entry.timestamp}")
                        }
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Panell inferior: Detall seleccionat
            if (selectedEntry != null) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Detall complet de la partida", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Usuari: ${selectedEntry!!.username}")
                        Text("Punts: ${selectedEntry!!.score}")
                        Text("Guanyada: ${if (selectedEntry!!.isGameWon) "Sí" else "No"}")
                        Text("Data: ${selectedEntry!!.timestamp}")
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Log de la partida:", style = MaterialTheme.typography.labelLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(selectedEntry!!.log, style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else {
                Text("Selecciona una partida per veure els detalls.", style = MaterialTheme.typography.bodyMedium)
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
