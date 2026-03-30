package com.practicum.playlistmaker.playlist.ui.newplaylist

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

open class NewPlaylistViewModel(
    private val application: Application,
    private val interactor: PlaylistInteractor
) : AndroidViewModel(application) {
    protected val imageUri = MutableLiveData<String>("")

    open fun saveImageToPrivateStorage(uri: Uri) {
        viewModelScope.launch {
            imageUri.value = interactor.saveImageToPrivateStorage(uri).absolutePath
        }
    }

    open fun savePlaylist(
        name: String,
        description: String
    ) {
        viewModelScope.launch {
            interactor.createPlaylist(
                Playlist(
                    name = name,
                    description = description,
                    uri = imageUri.value.toString()
                )
            )
        }
    }
}