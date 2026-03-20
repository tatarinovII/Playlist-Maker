package com.practicum.playlistmaker.player.models

import java.text.SimpleDateFormat
import java.util.Locale

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val progress: String, val isFavorite: Boolean) {
    class Default(isFavorite: Boolean) : PlayerState(false, SimpleDateFormat("mm:ss", Locale.getDefault()).format(0), isFavorite )
    class Prepared(isFavorite: Boolean) : PlayerState(true, SimpleDateFormat("mm:ss", Locale.getDefault()).format(0), isFavorite)
    class Playing(progress: String, isFavorite: Boolean) : PlayerState(true, progress, isFavorite)
    class Paused(progress: String, isFavorite: Boolean) : PlayerState(true, progress, isFavorite)
}
