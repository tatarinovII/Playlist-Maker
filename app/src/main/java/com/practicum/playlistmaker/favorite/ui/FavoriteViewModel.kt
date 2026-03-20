package com.practicum.playlistmaker.favorite.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorite.domain.FavoriteInteractor
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val favoriteState = MutableLiveData<FavoriteState>()
    fun observeFavoriteState(): MutableLiveData<FavoriteState> = favoriteState

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            favoriteInteractor.getAllTracks().collect { tracks ->
                if (tracks.isEmpty()) favoriteState.postValue(FavoriteState.Empty())
                else favoriteState.postValue(FavoriteState.Default(tracks))
            }
        }
    }
}