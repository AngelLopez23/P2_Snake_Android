package com.example.snake.service

import android.content.Context
import android.media.MediaPlayer
import com.example.snake.R

class SoundService(private val context: Context) {

    private var eatSound: MediaPlayer? = null
    private var gameOverSound: MediaPlayer? = null

    fun playEat() {
        releaseEat()
        eatSound = MediaPlayer.create(context, R.raw.eat_sound)
        eatSound?.start()
    }

    fun playGameOver() {
        releaseGameOver()
        gameOverSound = MediaPlayer.create(context, R.raw.game_over)
        gameOverSound?.start()
    }

    private fun releaseEat() {
        eatSound?.release()
        eatSound = null
    }

    private fun releaseGameOver() {
        gameOverSound?.release()
        gameOverSound = null
    }

    fun releaseAll() {
        releaseEat()
        releaseGameOver()
    }
}
