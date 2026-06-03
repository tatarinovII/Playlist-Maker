package com.practicum.playlistmaker.library.ui.playlist

import com.practicum.playlistmaker.playlist.domain.models.Playlist

sealed interface PlaylistState {

    data class Default(val list: List<Playlist>) : PlaylistState

    object Empty : PlaylistState
    object Loading: PlaylistState
}