package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface HistoryRepository {

    fun getHistory(): List<Track>

    fun saveTrack(track: Track): Boolean

    fun clearHistory()

}