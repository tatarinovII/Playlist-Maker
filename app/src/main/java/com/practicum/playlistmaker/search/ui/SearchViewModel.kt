package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.models.SearchScreenState
import com.practicum.playlistmaker.search.models.SearchState
import com.practicum.playlistmaker.utils.Resource

class SearchViewModel(
    private val historyInteractor: HistoryInteractor, private val trackInteractor: TrackInteractor
) : ViewModel() {

    private val searchRunnable = Runnable { searchRequest(lastSearchText) }
    private val handler = Handler(Looper.getMainLooper())
    private val stateLiveData = MutableLiveData(
        SearchState(
            SearchScreenState.DEFAULT_STATE.state, emptyList(), emptyList()
        )
    )
    fun observeState(): LiveData<SearchState> = stateLiveData
    private var lastSearchText: String = ""
    fun getHistoryList() {
        when (val resource = historyInteractor.getHistory()) {
            is Resource.Success -> {
                if (resource != null) {
                    val currentState = stateLiveData.value ?: SearchState(
                        SearchScreenState.DEFAULT_STATE.state, emptyList(), emptyList()
                    )
                    if (currentState.tracksHistory == resource.data) return
                    stateLiveData.postValue(currentState.copy(tracksHistory = resource.data ?: emptyList()))
                }
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
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun onButtonClearHistoryClicked() {
        historyInteractor.clearHistory()
        getHistoryList()
    }

    fun search(searchText: String) {
        this.lastSearchText = searchText
        handler.removeCallbacks(searchRunnable)
        searchRequest(lastSearchText)
    }

    private fun searchRequest(expression: String) {
        if (expression.isEmpty()) return

        val currentState = stateLiveData.value ?: SearchState(
            SearchScreenState.DEFAULT_STATE.state, emptyList(), emptyList()
        )
        stateLiveData.postValue(currentState.copy(state = SearchScreenState.LOADING_STATE.state))

        trackInteractor.searchTracks(
            expression, object : TrackInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?) {
                    handler.post {
                        val lastState = stateLiveData.value ?: SearchState(
                            SearchScreenState.DEFAULT_STATE.state, emptyList(), emptyList()
                        )
                        if (foundTracks != null) {
                            val newState = if (foundTracks.isNotEmpty()) {
                                lastState.copy(
                                    tracksSearch = foundTracks,
                                    state = SearchScreenState.RESULT_STATE.state
                                )
                            } else {
                                lastState.copy(
                                    tracksSearch = emptyList(),
                                    state = SearchScreenState.EMPTY_RESULT_STATE.state
                                )

                            }
                            stateLiveData.value = newState
                        } else {
                            stateLiveData.value =
                                lastState.copy(state = SearchScreenState.CONNECTION_ERROR_STATE.state)
                        }
                    }
                }

            })
    }

    fun clearTracks() {
        val currentState = stateLiveData.value ?: SearchState(
            SearchScreenState.DEFAULT_STATE.state, emptyList(), emptyList()
        )
        stateLiveData.postValue(currentState.copy(tracksSearch = emptyList()))
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}