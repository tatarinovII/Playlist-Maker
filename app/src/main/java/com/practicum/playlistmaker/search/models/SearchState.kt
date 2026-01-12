package com.practicum.playlistmaker.search.models

import com.practicum.playlistmaker.search.domain.models.Track

data class SearchState (
    var state: Int,
    var tracksSearch: List<Track>,
    var tracksHistory: List<Track>
)