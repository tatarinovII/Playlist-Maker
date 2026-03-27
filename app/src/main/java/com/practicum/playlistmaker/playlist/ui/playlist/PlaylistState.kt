package com.practicum.playlistmaker.playlist.ui.playlist

import com.practicum.playlistmaker.playlist.domain.models.Playlist

sealed class PlaylistState(val list: List<Playlist> = emptyList<Playlist>()) {

    class Default(list: List<Playlist>) : PlaylistState(list)

    class Empty() : PlaylistState()

}