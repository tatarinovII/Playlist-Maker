package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.utils.Resource
import com.practicum.playlistmaker.search.domain.HistoryInteractor
import com.practicum.playlistmaker.search.domain.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(val historyRepository: HistoryRepository) : HistoryInteractor {
    override suspend fun getHistory(): Resource<List<Track>> {
        return historyRepository.getHistory()
    }

    override fun addTrackToHistory(track: Track): Boolean {
        return historyRepository.saveTrack(track)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

}