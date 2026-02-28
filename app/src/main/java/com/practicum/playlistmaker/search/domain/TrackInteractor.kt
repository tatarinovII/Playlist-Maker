package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {

    fun searchTracks(expression: String) : Flow<Pair<List<Track>?, String?>>
}