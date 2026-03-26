package com.practicum.playlistmaker.playlist.ui

import com.practicum.playlistmaker.favorite.ui.FavoriteState
import com.practicum.playlistmaker.playlist.domain.Playlist

sealed class PlaylistState(val list: List<Playlist> = emptyList<Playlist>()) {

    class Default(list: List<Playlist>) : PlaylistState(list)

    class Empty() : PlaylistState()

}