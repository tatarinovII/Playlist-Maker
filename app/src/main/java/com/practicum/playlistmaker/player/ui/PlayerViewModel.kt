package com.practicum.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.player.models.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track?
) : ViewModel() {
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            updateCurrentTime()
            handler.postDelayed(this, 300L)
        }
    }
    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer = MediaPlayer()
    private val playerStateLiveData = MutableLiveData(PlayerState(STATE_DEFAULT, "00:00"))
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    init {
        preparePlayer()
    }

    fun preparePlayer() {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            val currentState = playerStateLiveData.value ?: PlayerState(STATE_DEFAULT, "00:00")
            playerStateLiveData.postValue(currentState.copy(state = STATE_PREPARED))
        }
        mediaPlayer.setOnCompletionListener {
            val currentState = playerStateLiveData.value ?: PlayerState(STATE_DEFAULT, "00:00")
            playerStateLiveData.postValue(currentState.copy(state = STATE_PREPARED, timeProgress = "00:00"))
            handler.removeCallbacks(updateTimeRunnable)
        }
    }

    fun updateCurrentTime() {
        if (playerStateLiveData.value?.state == STATE_PLAYING) {
            val newTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            val currentState = playerStateLiveData.value ?: return
            playerStateLiveData.postValue(currentState.copy(timeProgress = newTime))
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        handler.post(updateTimeRunnable)
        val currentState = playerStateLiveData.value ?: return
        playerStateLiveData.postValue(currentState.copy(state = STATE_PLAYING))
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        val currentState = playerStateLiveData.value ?: return
        playerStateLiveData.postValue(currentState.copy(state = STATE_PAUSED))
        handler.removeCallbacks(updateTimeRunnable)
    }

    fun playbackControl() {
        when (playerStateLiveData.value?.state) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
            else -> {

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        handler.removeCallbacks(updateTimeRunnable)
    }

    companion object {
        fun getFactory(track: Track?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(track)
            }
        }

        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}