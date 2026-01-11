package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track

interface TrackRepository {
    fun searchTracks(expression: String) : List<Track>
}