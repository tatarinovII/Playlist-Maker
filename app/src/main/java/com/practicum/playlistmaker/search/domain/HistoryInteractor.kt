package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.utils.Resource
import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryInteractor {

    suspend fun getHistory(): Resource<List<Track>>
    fun addTrackToHistory(track: Track): Boolean
    fun clearHistory()
}