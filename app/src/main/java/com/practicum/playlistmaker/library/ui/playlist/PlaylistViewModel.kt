package com.practicum.playlistmaker.library.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {

    init {
        getAllPlaylists()
    }

    private val _state = MutableStateFlow<PlaylistState>(PlaylistState.Loading)
    val state: StateFlow<PlaylistState> = _state.asStateFlow()

    fun getAllPlaylists() {
        viewModelScope.launch {
            interactor.getAllPlaylists().collect { playlists ->
                _state.value = if (playlists.isEmpty()) PlaylistState.Empty
                else PlaylistState.Default(playlists)
            }
        }
    }
}
