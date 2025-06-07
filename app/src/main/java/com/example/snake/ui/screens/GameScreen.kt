package com.example.snake.ui.screens

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.snake.R
import com.example.snake.model.Direction
import com.example.snake.model.FieldSize
import com.example.snake.viewmodel.GameViewModel
import com.example.snake.viewmodel.SettingsViewModel
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snake.model.GameSettings
import androidx.xr.compose.testing.toDp
import com.example.snake.data.AppDatabase
import com.example.snake.data.GameResult
import com.example.snake.model.GridPosition
import com.example.snake.model.Snake
import kotlin.concurrent.timer

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel
) {
    val settings by settingsViewModel.settings.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val gameState = gameViewModel.gameState
    val fieldSize = settings.fieldSize
    var boxSizePx by remember { mutableStateOf(IntSize.Zero) }
    val cellSizePx = remember(boxSizePx, fieldSize) {
        if (fieldSize.width != 0 && fieldSize.height != 0)
            IntSize(
                width = boxSizePx.width / fieldSize.width,
                height = boxSizePx.height / fieldSize.height
            )
        else IntSize.Zero
    }

    //Posar if per control de si ens trobem en mitja partida, si no ens trobem en mitja partida podrem reiniciar la logica del joc
    //Lógica de reinici del joc si les ajustes cambien
    LaunchedEffect(settingsViewModel.settings) {
        if(!gameViewModel.playing){
            gameViewModel.startGame(settings)
        }
    }
    if (isLandscape) { // landscape
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image( // imatge fondo
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                //Mapa
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(fieldSize.width.toFloat() / fieldSize.height.toFloat())
                        .onGloballyPositioned { coordinates ->
                            boxSizePx = coordinates.size
                        }
                ) {
                    val backgroundRes = when (fieldSize) {
                        FieldSize.SMALL -> R.drawable.map_10x10_snake
                        FieldSize.MEDIUM -> R.drawable.map_15x15_snake
                        FieldSize.LARGE -> R.drawable.map_20x20_snake
                    }

                    val headPainter = painterResource(id = R.drawable.snake_head)
                    val bodyPainter = painterResource(id = R.drawable.snake_body)
                    val tailPainter = painterResource(id = R.drawable.snake_tail)
                    val foodPainter = painterResource(id = R.drawable.apple_snake)

                    Image(
                        painter = painterResource(id = backgroundRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    // Dibuixar el menjar
                    DrawFood(
                        foodPosition = gameState.food,
                        cellSizePx = cellSizePx,
                        foodPainter = foodPainter
                    )
                    // Dibuixar la serp
                    DrawSnake(
                        snake = gameState.snake,
                        cellSizePx = cellSizePx,
                        headPainter = headPainter,
                        bodyPainter = bodyPainter,
                        tailPainter = tailPainter
                    )
                }

                // COLUMNA (puntuació, tiemps, botó enviar)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Puntuació y Timer
                    ScoreAndTimerCard(
                        score = gameState.score,
                        remainingTime = gameViewModel.remainingTime,
                        timerEnabled = settings.timerEnabled,
                        isGameOver = gameState.isGameOver
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    if (!gameState.isGameOver) {
                        //LOG
                        LogDisplay(log = gameViewModel.latestLog, isLandscape = isLandscape)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Mensaje victoria / derrota
                    VictoryDefeatEmailSection(
                        isGameOver = gameState.isGameOver,
                        isGameWon = gameState.isGameWon,
                        score = gameState.score,
                        username = settings.username,
                        recipientEmail = settings.recipientEmail,
                        gameViewModel=gameViewModel,
                        timerEnabled = settings.timerEnabled
                    )
                }
                if (!gameState.isGameOver) {
                    // COLUMNA (botons)
                    DirectionControls { direction ->
                        gameViewModel.changeDirection(direction)
                    }
                }
            }
        }
    }else{//portrait
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Image(//imatge fondo
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                //Puntuació i Timer
                ScoreAndTimerCard(
                    score = gameState.score,
                    remainingTime = gameViewModel.remainingTime,
                    timerEnabled = settings.timerEnabled,
                    isGameOver = gameState.isGameOver
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(fieldSize.width.toFloat() / fieldSize.height.toFloat())
                        .onGloballyPositioned { coordinates ->
                            boxSizePx = coordinates.size
                        }
                ) {
                    val backgroundRes = when (fieldSize) {
                        FieldSize.SMALL -> R.drawable.map_10x10_snake
                        FieldSize.MEDIUM -> R.drawable.map_15x15_snake
                        FieldSize.LARGE -> R.drawable.map_20x20_snake
                    }

                    val headPainter = painterResource(id = R.drawable.snake_head)
                    val bodyPainter = painterResource(id = R.drawable.snake_body)
                    val tailPainter = painterResource(id = R.drawable.snake_tail)
                    val foodPainter = painterResource(id = R.drawable.apple_snake)

                    Image(
                        painter = painterResource(id = backgroundRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Dibuixar el menjar
                    DrawFood(
                        foodPosition = gameState.food,
                        cellSizePx = cellSizePx,
                        foodPainter = foodPainter
                    )


                    // Dibuixar la serp
                    DrawSnake(
                        snake = gameState.snake,
                        cellSizePx = cellSizePx,
                        headPainter = headPainter,
                        bodyPainter = bodyPainter,
                        tailPainter = tailPainter
                    )
                }

                //Missatge victoria / derrota
                VictoryDefeatEmailSection(
                    isGameOver = gameState.isGameOver,
                    isGameWon = gameState.isGameWon,
                    score = gameState.score,
                    username = settings.username,
                    recipientEmail = settings.recipientEmail,
                    gameViewModel=gameViewModel,
                    timerEnabled = settings.timerEnabled
                )

                Spacer(modifier = Modifier.height(16.dp))
                if (!gameState.isGameOver) {
                    //LOG
                    LogDisplay(log = gameViewModel.latestLog, isLandscape = isLandscape)

                    Spacer(modifier = Modifier.height(16.dp))

                    // COLUMNA (botons)
                    DirectionControls { direction ->
                        gameViewModel.changeDirection(direction)
                    }
                }
            }
        }
    }
}

///////////////////////////////// FUNCIONS /////////////////////////////////////////////////////////

@Composable
fun LogDisplay(log: String, isLandscape: Boolean) {
    if (log.isNotEmpty()) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .then(
                    if (isLandscape) Modifier.widthIn(max = 200.dp)
                    else Modifier.fillMaxWidth()
                ),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                text = log,
                modifier = Modifier.padding(8.dp),
                color = Color.Black
            )
        }
    }
}

@Composable
fun DirectionButton(icon: ImageVector, contentDescription: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = Color.Black,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun DirectionControls(onDirectionChange: (Direction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DirectionButton(
            icon = Icons.Default.KeyboardArrowUp,
            contentDescription = stringResource(R.string.direction_up)
        ) {
            onDirectionChange(Direction.UP)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DirectionButton(
                icon = Icons.Default.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.direction_left)
            ) {
                onDirectionChange(Direction.LEFT)
            }

            Spacer(modifier = Modifier.width(32.dp))

            DirectionButton(
                icon = Icons.Default.KeyboardArrowRight,
                contentDescription = stringResource(R.string.direction_right)
            ) {
                onDirectionChange(Direction.RIGHT)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        DirectionButton(
            icon = Icons.Default.KeyboardArrowDown,
            contentDescription = stringResource(R.string.direction_down)
        ) {
            onDirectionChange(Direction.DOWN)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun DrawFood(
    foodPosition: GridPosition,
    cellSizePx: IntSize,
    foodPainter: Painter
) {
    Image(
        painter = foodPainter,
        contentDescription = stringResource(R.string.apple_description),
        modifier = Modifier
            .size(
                width = cellSizePx.width.toDp(),
                height = cellSizePx.height.toDp()
            )
            .offset {
                IntOffset(
                    x = cellSizePx.width * foodPosition.x,
                    y = cellSizePx.height * foodPosition.y
                )
            }
    )
}


@Composable
fun ScoreAndTimerCard(
    score: Int,
    remainingTime: Int,
    timerEnabled: Boolean,
    isGameOver: Boolean
) {
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Puntuación: $score",
                color = Color.Black
            )
            if (timerEnabled) {
                Text(
                    text = "Tiempo restante: $remainingTime s",
                    color = Color.Black
                )
            }
        }
    }
}


@Composable
fun VictoryDefeatEmailSection(
    isGameOver: Boolean,
    isGameWon: Boolean,
    score: Int,
    username: String,
    recipientEmail: String,
    gameViewModel:GameViewModel,
    timerEnabled: Boolean
) {
    if (!isGameOver) return

    val message = if (isGameWon)
        stringResource(id = R.string.game_win)
    else
        stringResource(id = R.string.game_over)

    val backgroundColor = if (isGameWon) Color.Yellow else Color.Red
    val textColor = if (isGameWon) Color.Black else Color.White

    // Obtener el contexto para acceder a la base de datos
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context) // Obtener la instancia de la base de datos
    val gameResultDao = db.gameResultDao()

    // Guardar el resultado en la base de datos
    LaunchedEffect(Unit) {
        val gameResult = GameResult(
            username = username,
            recipientEmail = recipientEmail,
            score = score,
            isGameWon = isGameWon,
            remainingTime = gameViewModel.remainingTime,
            logContent = gameViewModel.getFullLog()
        )
        // Insertar el resultado en la base de datos
        gameResultDao.insertGameResult(gameResult)
    }

    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = message,
                color = textColor,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        SendResultEmailButton(
            isGameWon = isGameWon,
            isGameOver = isGameOver,
            score = score,
            username = username,
            recipientEmail = recipientEmail,
            logContent = gameViewModel.getFullLog(),
            remainingTime = gameViewModel.remainingTime,
            timerEnabled = timerEnabled
        )

    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun DrawSnake(
    snake: Snake,
    cellSizePx: IntSize,
    headPainter: Painter,
    bodyPainter: Painter,
    tailPainter: Painter
) {
    snake.body.forEachIndexed { index, position ->
        val painter: Painter
        val rotation: Float

        when (index) {
            0 -> { // Cap
                painter = headPainter
                rotation = when (snake.direction) {
                    Direction.UP -> 0f
                    Direction.DOWN -> 180f
                    Direction.LEFT -> 270f
                    Direction.RIGHT -> 90f
                }
            }

            snake.body.lastIndex -> { // Cua
                painter = tailPainter
                val prev = snake.body[index - 1]
                rotation = when {
                    prev.x < position.x -> 90f
                    prev.x > position.x -> 270f
                    prev.y < position.y -> 180f
                    else -> 0f
                }
            }

            else -> { // Cos
                painter = bodyPainter
                val prev = snake.body[index - 1]
                val next = snake.body[index + 1]
                rotation = when {
                    prev.x == next.x -> 180f
                    prev.y == next.y -> 90f
                    else -> 0f
                }
            }
        }

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(
                    width = cellSizePx.width.toDp(),
                    height = cellSizePx.height.toDp()
                )
                .offset {
                    IntOffset(
                        x = cellSizePx.width * position.x,
                        y = cellSizePx.height * position.y
                    )
                }
                .graphicsLayer {
                    rotationZ = rotation
                }
        )
    }
}



@Composable
fun SendResultEmailButton(
    isGameWon: Boolean,
    isGameOver: Boolean,
    score: Int,
    username: String,
    recipientEmail: String,
    logContent: String,
    remainingTime: Int,
    timerEnabled: Boolean
) {
    val context = LocalContext.current

    Button(onClick = {
        val subjectResId = if (isGameWon) R.string.email_subject_win else R.string.email_subject_lose
        val subject = context.getString(subjectResId)

        val resultText = if (isGameWon)
            context.getString(R.string.email_result_win)
        else
            context.getString(R.string.email_result_lose)

        val message = context.getString(
            R.string.email_body,
            username,
            score,
            resultText
        ) + (if (timerEnabled) "\n\nTemps restant: $remainingTime" else " ") + "\n\nLog:\n$logContent"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            context.startActivity(
                Intent.createChooser(
                    intent,
                    context.getString(R.string.email_chooser_title)
                )
            )
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.email_no_app),
                Toast.LENGTH_SHORT
            ).show()
        }
    }) {
        Text(stringResource(R.string.settings_send))
    }
}

