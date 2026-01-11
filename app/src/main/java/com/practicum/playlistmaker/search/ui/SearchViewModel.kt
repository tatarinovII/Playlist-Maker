package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.Resource

class SearchViewModel(
    private val historyInteractor: HistoryInteractor, private val trackInteractor: TrackInteractor
) : ViewModel() {

    private val searchRunnable = Runnable { searchRequest(lastSearchText)}
    private val handler = Handler(Looper.getMainLooper())
    private val state = MutableLiveData(DEFAULT_STATE)
    fun observeState(): LiveData<Int> = state
    private val tracksList = MutableLiveData(emptyList<Track>())
    fun observeTracks(): LiveData<List<Track>> = tracksList
    private var lastSearchText: String = ""
    fun getHistoryList(): List<Track> {
        when (val resource = historyInteractor.getHistory()) {
            is Resource.Success -> {
                if (resource != null) {
                    return resource.data ?: emptyList()
                }
            }

            is Resource.Error -> {
                return emptyList()
            }
        }
        return emptyList()
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

    fun onButtonClearHistoryClicked(): List<Track> {
        historyInteractor.clearHistory()
        return getHistoryList()
    }

    fun search(searchText: String) {
        this.lastSearchText = searchText
        handler.removeCallbacks(searchRunnable)
        searchRequest(lastSearchText)
    }

    private fun searchRequest(expression: String) {
        if (expression.isEmpty()) return
        state.value = LOADING_STATE
        trackInteractor.searchTracks(
            expression, object : TrackInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?) {
                    handler.post {
                        if (foundTracks != null) {
                            tracksList.value = foundTracks
                            if (foundTracks.isNotEmpty()) {
                                state.value = RESULT_STATE
                            } else {
                                state.value = EMPTY_RESULT_STATE
                            }
                        } else {
                            state.value = CONNECTION_ERROR_STATE
                        }
                    }
                }

            })
    }

    fun clearTracks() {
        tracksList.value = emptyList<Track>()
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val LOADING_STATE = 0
        private const val CONNECTION_ERROR_STATE = 1
        private const val EMPTY_RESULT_STATE = 2
        private const val RESULT_STATE = 3
        private const val DEFAULT_STATE = 4
        fun getFactory(
            historyInteractor: HistoryInteractor, trackInteractor: TrackInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(historyInteractor, trackInteractor)
            }
        }
    }
}