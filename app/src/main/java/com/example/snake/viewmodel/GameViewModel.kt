package com.example.snake.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.snake.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.snake.data.local.GameHistoryEntity
import com.example.snake.data.local.GameHistoryRepository
import com.example.snake.service.SoundService
import java.text.SimpleDateFormat
import java.util.Date


class GameViewModel(
    application: Application,
    private val soundService: SoundService,
    private val repository: GameHistoryRepository
) : AndroidViewModel(application) {

    private var gameJob: Job? = null
    private var timerJob: Job? = null
    var playing by mutableStateOf(false)
        private set

    var remainingTime by mutableStateOf(60)
        private set

    var gameState by mutableStateOf(
        GameState(
            snake = Snake(
                body = listOf(GridPosition(5, 5)),
                direction = Direction.RIGHT
            ),
            food = GridPosition(7, 7)
        )
    )
        private set

    var settings = GameSettings()
        private set

    var latestLog by mutableStateOf("")
        private set

    private val logHistory = mutableListOf<String>()

    fun getFullLog(): String {
        return logHistory.joinToString("\n")
    }

    private var startTime: Long = 0L  // en mil·lisegons

    fun startGame() {
        startTime = System.currentTimeMillis()
        logHistory.clear()
        latestLog = ""
    }

    private fun logEvent(event: String) {
        val elapsedMillis = System.currentTimeMillis() - startTime

        val minutes = (elapsedMillis / 1000) / 60
        val seconds = (elapsedMillis / 1000) % 60

        val currentTime = String.format("%02d:%02d", minutes, seconds)

        val log = "$currentTime $event"
        latestLog = log
        logHistory.add(log)
    }


    fun startGame(settings: GameSettings) {
        logHistory.clear()
        latestLog = ""
        playing = true
        startGame()
        this.settings = settings
        gameState = GameState(
            snake = Snake(
                body = listOf(GridPosition(settings.fieldSize.width / 2, settings.fieldSize.height / 2)),
                direction = Direction.RIGHT
            ),
            food = generateFood()
        )
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            while (!gameState.isGameOver) {
                delay(settings.difficulty.speed)
                moveSnake()
            }
        }
        if (settings.timerEnabled) {
            timerJob?.cancel()
            remainingTime = 60
            timerJob = viewModelScope.launch {
                while (remainingTime >= 0) {
                    delay(1000L)
                    if (playing){
                        remainingTime--
                    }
                }
                gameState = gameState.copy(isGameOver = true)
            }
        }
    }

    private fun moveSnake() {
        val head = gameState.snake.body.first()

        val newHead = when (gameState.snake.direction) {
            Direction.UP -> GridPosition(head.x, head.y - 1)
            Direction.DOWN -> GridPosition(head.x, head.y + 1)
            Direction.LEFT -> GridPosition(head.x - 1, head.y)
            Direction.RIGHT -> GridPosition(head.x + 1, head.y)
        }

        // Colisión → derrota
        if (newHead.x < 0 || newHead.y < 0 ||
            newHead.x >= settings.fieldSize.width || newHead.y >= settings.fieldSize.height ||
            gameState.snake.body.contains(newHead)
        ) {
            logEvent("Defeat")
            soundService.playGameOver()
            gameState = gameState.copy(isGameOver = true)
            playing = false
            saveGameHistory()
            return
        }

        val newBody = mutableListOf(newHead)
        newBody.addAll(gameState.snake.body)

        // Comió la comida
        if (newHead == gameState.food) {
            logEvent("Snake eat apple")
            soundService.playEat()
            val maxCells = settings.fieldSize.width * settings.fieldSize.height
            val didWin = newBody.size == maxCells

            gameState = gameState.copy(
                snake = gameState.snake.copy(body = newBody),
                food = if (didWin) gameState.food else generateFood(),
                score = gameState.score + 1,
                isGameOver = didWin,
                isGameWon = didWin
            )

            if (didWin) {
                soundService.playGameOver()
                saveGameHistory()
            }

        } else {
            newBody.removeAt(newBody.lastIndex)
            gameState = gameState.copy(
                snake = gameState.snake.copy(body = newBody)
            )
        }
    }

    private fun generateFood(): GridPosition {
        val occupied = gameState.snake.body.toSet()
        val width = settings.fieldSize.width
        val height = settings.fieldSize.height
        val available = mutableListOf<GridPosition>()
        for (x in 0 until width) {
            for (y in 0 until height) {
                val pos = GridPosition(x, y)
                if (!occupied.contains(pos)) {
                    available.add(pos)
                }
            }
        }
        return if (available.isNotEmpty()) {
            available[Random.nextInt(available.size)]
        } else {
            GridPosition(0, 0)
        }
    }

    fun changeDirection(direction: Direction) {
        val current = gameState.snake.direction
        if ((current == Direction.UP && direction != Direction.DOWN) ||
            (current == Direction.DOWN && direction != Direction.UP) ||
            (current == Direction.LEFT && direction != Direction.RIGHT) ||
            (current == Direction.RIGHT && direction != Direction.LEFT)
        ) {
            gameState = gameState.copy(
                snake = gameState.snake.copy(direction = direction)
            )
            logEvent("Snake turn ${direction.name.lowercase()}")
        }
    }

    private fun saveGameHistory() {
        viewModelScope.launch {
            val entity = GameHistoryEntity(
                username = settings.username,
                score = gameState.score,
                isGameWon = gameState.isGameWon,
                timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
                log = getFullLog()
            )
            repository.save(entity)
        }
    }

}
