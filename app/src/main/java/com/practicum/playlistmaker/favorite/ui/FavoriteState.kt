package com.practicum.playlistmaker.favorite.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavoriteState {

    data class Default(val tracks: List<Track>) : FavoriteState
    data object Loading : FavoriteState
    data object Empty : FavoriteState
}