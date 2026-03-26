package com.practicum.playlistmaker.playlist.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {

    init {
        getAllPlaylists()
    }

    private val _state = MutableLiveData<PlaylistState>()
    fun observePlaylistState(): MutableLiveData<PlaylistState> = _state

    fun getAllPlaylists() {
        viewModelScope.launch {
            interactor.getAllPlaylists().collect { playlists ->
                if (playlists.isEmpty()) _state.postValue(PlaylistState.Empty())
                else _state.postValue(PlaylistState.Default(playlists))
            }
        }
    }

}