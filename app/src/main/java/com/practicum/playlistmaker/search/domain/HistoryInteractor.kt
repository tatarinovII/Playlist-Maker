package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.utils.Resource
import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryInteractor {

    fun getHistory(): Resource<List<Track>>
    fun addTrackToHistory(track: Track): Boolean
    fun clearHistory()
}