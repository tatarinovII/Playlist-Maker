package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.utils.Resource
import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryRepository {

    suspend fun getHistory(): Resource<List<Track>>

    fun saveTrack(track: Track): Boolean

    fun clearHistory()

}