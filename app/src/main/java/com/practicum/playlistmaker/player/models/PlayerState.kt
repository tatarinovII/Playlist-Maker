package com.practicum.playlistmaker.player.models

import com.practicum.playlistmaker.playlist.domain.models.Playlist
import java.text.SimpleDateFormat
import java.util.Locale

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val progress: String, val isFavorite: Boolean, val listOfPlaylists: List<Playlist> = emptyList<Playlist>()) {
    class Default(isFavorite: Boolean, list: List<Playlist> = emptyList<Playlist>()) : PlayerState(false, SimpleDateFormat("mm:ss", Locale.getDefault()).format(0), isFavorite, list )
    class Prepared(isFavorite: Boolean, list: List<Playlist> = emptyList<Playlist>()) : PlayerState(true, SimpleDateFormat("mm:ss", Locale.getDefault()).format(0), isFavorite, list)
    class Playing(progress: String, isFavorite: Boolean, list: List<Playlist> = emptyList<Playlist>()) : PlayerState(true, progress, isFavorite, list)
    class Paused(progress: String, isFavorite: Boolean, list: List<Playlist> = emptyList<Playlist>()) : PlayerState(true, progress, isFavorite, list)
}
