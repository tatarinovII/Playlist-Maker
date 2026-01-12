package com.practicum.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.models.MediaPlayerState
import com.practicum.playlistmaker.player.models.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track?, private val mediaPlayer: MediaPlayer
) : ViewModel() {
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            updateCurrentTime()
            handler.postDelayed(this, 300L)
        }
    }
    private val handler = Handler(Looper.getMainLooper())
    private val playerStateLiveData =
        MutableLiveData(PlayerState(MediaPlayerState.STATE_DEFAULT.state, "00:00"))

    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    init {
        preparePlayer()
    }

    fun preparePlayer() {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            val currentState = playerStateLiveData.value ?: PlayerState(
                MediaPlayerState.STATE_DEFAULT.state, "00:00"
            )
            playerStateLiveData.postValue(currentState.copy(state = MediaPlayerState.STATE_PREPARED.state))
        }
        mediaPlayer.setOnCompletionListener {
            val currentState = playerStateLiveData.value ?: PlayerState(
                MediaPlayerState.STATE_DEFAULT.state, "00:00"
            )
            playerStateLiveData.postValue(
                currentState.copy(
                    state = MediaPlayerState.STATE_PREPARED.state, timeProgress = "00:00"
                )
            )
            handler.removeCallbacks(updateTimeRunnable)
        }
    }

    fun updateCurrentTime() {
        if (playerStateLiveData.value?.state == MediaPlayerState.STATE_PLAYING.state) {
            val newTime =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            val currentState = playerStateLiveData.value ?: return
            playerStateLiveData.postValue(currentState.copy(timeProgress = newTime))
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        handler.post(updateTimeRunnable)
        val currentState = playerStateLiveData.value ?: return
        playerStateLiveData.postValue(currentState.copy(state = MediaPlayerState.STATE_PLAYING.state))
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        val currentState = playerStateLiveData.value ?: return
        playerStateLiveData.postValue(currentState.copy(state = MediaPlayerState.STATE_PAUSED.state))
        handler.removeCallbacks(updateTimeRunnable)
    }

    fun playbackControl() {
        when (playerStateLiveData.value?.state) {
            MediaPlayerState.STATE_PLAYING.state -> {
                pausePlayer()
            }

            MediaPlayerState.STATE_PREPARED.state, MediaPlayerState.STATE_PAUSED.state -> {
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
}