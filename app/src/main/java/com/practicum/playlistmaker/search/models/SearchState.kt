package com.practicum.playlistmaker.search.models

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface SearchState {
    data class ShowOutput(val searchOutput: List<Track>) : SearchState
    object InternetConnectionError : SearchState
    object EmptyOutput : SearchState
    data class ShowSearchHistory(val historyList: List<Track>) : SearchState
    object Default : SearchState
    object Loading: SearchState
}