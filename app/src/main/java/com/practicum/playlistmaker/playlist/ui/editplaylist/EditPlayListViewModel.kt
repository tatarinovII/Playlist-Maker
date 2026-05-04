package com.practicum.playlistmaker.playlist.ui.editplaylist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.playlist.ui.newplaylist.NewPlaylistViewModel
import kotlinx.coroutines.launch

class EditPlayListViewModel(
    application: Application, private val interactor: PlaylistInteractor
) : NewPlaylistViewModel(application, interactor) {

    private val _data =  MutableLiveData<Playlist>()
    fun observeData(): LiveData<Playlist> = _data

    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            val playlist = interactor.getPlaylistById(playlistId)
            _data.postValue(playlist)
        }
    }

    override fun savePlaylist(name: String, description: String) {
        val currentPlaylist = _data.value
        var imageUri = super.imageUri.value!!
        viewModelScope.launch {
            if (imageUri.isEmpty()) imageUri = currentPlaylist!!.uri
            interactor.editPlaylist(
                currentPlaylist!!.copy(
                    name = name,
                    description = description,
                    uri = imageUri
                )
            )
        }
    }
}