package com.practicum.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorite.domain.FavoriteInteractor
import com.practicum.playlistmaker.player.models.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private val mediaPlayer: MediaPlayer,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val playerState =
        MutableLiveData<PlayerState>(PlayerState.Default(isFavorite = track.isFavorite))

    fun observePlayerState(): LiveData<PlayerState> = playerState

    private var timerJob: Job? = null

    init {
        preparePlayer()
    }

    fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.postValue(PlayerState.Prepared(track.isFavorite))
        }
        mediaPlayer.setOnCompletionListener {
            playerState.postValue(PlayerState.Prepared(track.isFavorite))
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition(), track.isFavorite))
        startTimer()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition(), track.isFavorite))
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
        playerState.postValue(PlayerState.Default(track.isFavorite))
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
                playerState.postValue(
                    PlayerState.Playing(
                        getCurrentPlayerPosition(), track.isFavorite
                    )
                )
            }
            playerState.postValue(PlayerState.Prepared(track.isFavorite))
        }
    }

    fun onButtonLikeClicked() {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteInteractor.deleteTrack(track)
                changeIsFavoriteValue(false)
            } else {
                favoriteInteractor.addTrack(track)
                changeIsFavoriteValue(true)
            }
        }
    }

    private fun changeIsFavoriteValue(isFavorite: Boolean) {
        track.isFavorite = isFavorite
        val currentState = playerState.value

        val newState = when (currentState) {
            is PlayerState.Default -> PlayerState.Default(isFavorite)
            is PlayerState.Prepared -> PlayerState.Prepared(isFavorite)
            is PlayerState.Playing -> PlayerState.Playing(currentState.progress, isFavorite)
            is PlayerState.Paused -> PlayerState.Paused(currentState.progress, isFavorite)
            else -> currentState
        }

        playerState.postValue(newState!!)
    }

    companion object {
        const val REFRESH_TIMER_DELAY = 300L
    }
}