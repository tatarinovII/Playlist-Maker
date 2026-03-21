package com.practicum.playlistmaker.favorite.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed class FavoriteState(val tracks: List<Track> = emptyList<Track>()) {

    class Default(tracks: List<Track>) : FavoriteState(tracks)
    class Empty() : FavoriteState(emptyList())
}