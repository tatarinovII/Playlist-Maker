package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.models.SearchScreenState
import com.practicum.playlistmaker.search.models.SearchState
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val historyInteractor: HistoryInteractor, private val trackInteractor: TrackInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData(
        SearchState(
            SearchScreenState.DEFAULT_STATE.state, emptyList(), emptyList()
        )
    )
    fun observeState(): LiveData<SearchState> = stateLiveData
    private var lastSearchText: String = ""
    private var searchJob: Job? = null
    fun getHistoryList() {
        when (val resource = historyInteractor.getHistory()) {
            is Resource.Success -> {
                val currentState = stateLiveData.value ?: SearchState(
                    SearchScreenState.DEFAULT_STATE.state, emptyList(), emptyList()
                )
                if (currentState.tracksHistory == resource.data) return
                stateLiveData.postValue(
                    currentState.copy(
                        tracksHistory = resource.data ?: emptyList()
                    )
                )
            } else -> {
            val currentState = stateLiveData.value ?: SearchState(
                SearchScreenState.DEFAULT_STATE.state, emptyList(), emptyList()
            )
                stateLiveData.postValue(currentState.copy(tracksHistory = emptyList()))
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
        getHistoryList()
    }

    fun search(searchText: String) {
        this.lastSearchText = searchText
        searchJob?.cancel()
        searchRequest(lastSearchText)
    }

    private fun searchRequest(expression: String) {
        if (expression.isNotEmpty()) {
            stateLiveData.postValue(stateLiveData.value?.copy(SearchScreenState.LOADING_STATE.state))
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
                stateLiveData.postValue(stateLiveData.value?.copy(state = SearchScreenState.CONNECTION_ERROR_STATE.state))
            }

            foundTracks?.isEmpty() ?: true -> {
                stateLiveData.postValue(
                    stateLiveData.value?.copy(
                        state = SearchScreenState.EMPTY_RESULT_STATE.state,
                        tracksSearch = emptyList()
                    )
                )
            }

            else -> {
                stateLiveData.postValue(
                    stateLiveData.value?.copy(
                        state = SearchScreenState.RESULT_STATE.state, tracksSearch = tracks
                    )
                )
            }
        }
    }

    fun clearTracks() {
        val currentState = stateLiveData.value ?: SearchState(
            SearchScreenState.DEFAULT_STATE.state, emptyList(), emptyList()
        )
        stateLiveData.postValue(currentState.copy(tracksSearch = emptyList()))
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}