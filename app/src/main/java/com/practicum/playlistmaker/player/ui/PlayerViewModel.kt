package com.practicum.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorite.domain.FavoriteInteractor
import com.practicum.playlistmaker.player.models.PlayerState
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val playerState =
        MutableLiveData<PlayerState>(PlayerState.Default(isFavorite = track.isFavorite))

    fun observePlayerState(): LiveData<PlayerState> = playerState

    private var audioPlayerControl: AudioPlayerControl? = null

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect {
                playerState.postValue(it)
            }
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    override fun onCleared() {
        super.onCleared()
        removeAudioPlayerControl()
    }

    fun onPlayButtonClicked() {
        when (playerState.value) {
            is PlayerState.Playing -> {
                audioPlayerControl?.pausePlayer()
            }

            is PlayerState.Prepared, is PlayerState.Paused -> {
                audioPlayerControl?.startPlayer()
            }

            else -> {}
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
            is PlayerState.Prepared -> { PlayerState.Prepared(isFavorite) }
            is PlayerState.Playing -> PlayerState.Playing(currentState.progress, isFavorite)
            is PlayerState.Paused -> PlayerState.Paused(currentState.progress, isFavorite)
            else -> currentState
        }

        playerState.postValue(newState!!)
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect {
                val currentState = playerState.value
                val newState = when (currentState) {
                    is PlayerState.Default -> PlayerState.Default(currentState.isFavorite, it)
                    is PlayerState.Prepared -> PlayerState.Prepared(currentState.isFavorite, it)
                    is PlayerState.Playing -> PlayerState.Playing(currentState.progress, currentState.isFavorite, it)
                    is PlayerState.Paused -> PlayerState.Paused(currentState.progress, currentState.isFavorite, it)
                    else -> currentState
                }
                playerState.postValue(newState!!)
            }
        }
    }

    fun addTrackToPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addTrackToPlaylist(track, playlist)
            loadPlaylists()
        }
    }

    fun hideNotification() {
        audioPlayerControl?.hideNotification()
    }

    fun showNotification() {
        audioPlayerControl?.showNotification()
    }
}