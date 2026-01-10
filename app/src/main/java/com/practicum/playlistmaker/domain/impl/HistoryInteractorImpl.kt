package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.HistoryInteractor
import com.practicum.playlistmaker.domain.api.HistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class HistoryInteractorImpl(val historyRepository: HistoryRepository) : HistoryInteractor {
    override fun getHistory(): List<Track> {
        return historyRepository.getHistory()
    }

    override fun addTrackToHistory(track: Track): Boolean {
        return historyRepository.saveTrack(track)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

}