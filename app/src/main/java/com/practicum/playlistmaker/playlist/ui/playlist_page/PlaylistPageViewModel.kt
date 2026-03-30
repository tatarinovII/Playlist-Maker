package com.practicum.playlistmaker.playlist.ui.playlist_page

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import kotlinx.coroutines.launch

class PlaylistPageViewModel(
    playlistId: Long,
    private val interactor: PlaylistInteractor,
    private val context: Context,
    private val externalNavigator: ExternalNavigator
) : ViewModel() {

    init {
        loadPlaylistData(playlistId)
    }

    val state = MutableLiveData<PlaylistPageState>()
    fun observeState(): LiveData<PlaylistPageState> = state

    fun loadPlaylistData(playlistId: Long) {
        viewModelScope.launch {
            val playlist = interactor.getPlaylistById(playlistId)
            interactor.getAllTracksInPlaylist(playlist).collect {
                state.postValue(PlaylistPageState.Default(playlist, it))
            }
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            val currentState = state.value
            interactor.deleteTrackFromPlaylist(currentState!!.playlist, track)
            loadPlaylistData(currentState.playlist.id)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            interactor.deletePlaylist(state.value!!.playlist)
        }
    }

    fun sharePlayList() {
        externalNavigator.sharePlaylist(state.value!!.playlist, state.value!!.listOfTracks)
    }

}