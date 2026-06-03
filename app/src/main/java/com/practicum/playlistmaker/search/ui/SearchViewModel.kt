package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.HistoryInteractor
import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.models.SearchState
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val historyInteractor: HistoryInteractor, private val trackInteractor: TrackInteractor
) : ViewModel() {
    private val _state = MutableStateFlow<SearchState>(SearchState.Default)
    val state: StateFlow<SearchState> = _state
    private var lastSearchText: String = ""
    private var searchJob: Job? = null
    fun showSearchHistory() {
        viewModelScope.launch {
            when (val resource = historyInteractor.getHistory()) {
                is Resource.Success -> {
                    val data = resource.data ?: emptyList()
                    if (data.isNotEmpty()) _state.value = SearchState.ShowSearchHistory(data)
                    else _state.value = SearchState.Default
                }

                else -> {
                    _state.value = SearchState.Default
                }
            }
        }
    }

    fun addTrackToHistory(track: Track) {
        historyInteractor.addTrackToHistory(track)
    }

    fun searchDebounce(changedText: String) {
        if (changedText == lastSearchText) {
            return
        }
        this.lastSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    fun onButtonClearHistoryClicked() {
        historyInteractor.clearHistory()
        showSearchHistory()
    }

    fun search(searchText: String) {
        this.lastSearchText = searchText
        searchJob?.cancel()
        searchRequest(lastSearchText)
    }

    private fun searchRequest(expression: String) {
        if (expression.isNotEmpty()) {
            _state.value = SearchState.Loading
            viewModelScope.launch {
                trackInteractor.searchTracks(expression).collect { pair ->
                    processResult(pair.first, pair.second)
                }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) tracks.addAll(foundTracks)
        when {
            errorMessage != null -> {
                _state.value = SearchState.InternetConnectionError
            }

            foundTracks?.isEmpty() ?: true -> {
                _state.value = SearchState.EmptyOutput
            }

            else -> {
                _state.value = SearchState.ShowOutput(tracks)
            }
        }
    }

    fun clearTracks() {
        _state.value = SearchState.Default
        searchJob?.cancel()
        showSearchHistory()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}