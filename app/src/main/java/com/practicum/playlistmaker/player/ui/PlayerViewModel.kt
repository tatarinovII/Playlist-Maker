package com.practicum.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.models.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track, private val mediaPlayer: MediaPlayer
) : ViewModel() {

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())

    fun observePlayerState(): LiveData<PlayerState> = playerState

    private var timerJob: Job? = null

    init {
        preparePlayer()
    }

    fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.postValue(PlayerState.Prepared())
        }
        mediaPlayer.setOnCompletionListener {
            playerState.postValue(PlayerState.Prepared())
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    fun onPlayButtonClicked() {
        when (playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }

            else -> {}
        }
    }

    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState.postValue(PlayerState.Default())
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    private fun getCurrentPlayerPosition() : String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition) ?: "00:00"
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(REFRESH_TIMER_DELAY)
                playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
            playerState.postValue(PlayerState.Prepared())
        }
    }

    companion object {
        const val REFRESH_TIMER_DELAY = 300L
    }
}