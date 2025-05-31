package com.example.snake.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import com.example.snake.R

@Composable
fun HelpScreen() {
    val orientation = LocalConfiguration.current.orientation
    val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Panell esquerre: text
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.help_title),
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.help_description),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(id = R.string.help_controls_explanation),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Panell dret: imatges
            Column(
                modifier = Modifier.weight(0.1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imatge log del joc
                Image(
                    painter = painterResource(id = R.drawable.image_log_snake),
                    contentDescription = "Snake Game Logo",
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(2f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Imatge dels botons
                Image(
                    painter = painterResource(id = R.drawable.snake_butons),
                    contentDescription = stringResource(id = R.string.snake_controls_description),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .aspectRatio(1f)
                )
            }
        }
    } else {
        // Orientació vertical: tot en una sola columna
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.help_title),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.image_log_snake),
                contentDescription = "Snake Game Logo",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(2f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.help_description),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.snake_butons),
                contentDescription = stringResource(id = R.string.snake_controls_description),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.help_controls_explanation),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
