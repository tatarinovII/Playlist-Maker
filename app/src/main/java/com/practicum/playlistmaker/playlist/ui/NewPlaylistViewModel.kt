package com.practicum.playlistmaker.playlist.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.Playlist
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class NewPlaylistViewModel(
    private val application: Application,
    private val interactor: PlaylistInteractor
) : AndroidViewModel(application) {
    private val imageUri = MutableLiveData<String>("")

    fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${UUID.randomUUID()}.jpg")
        val inputStream = application.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        imageUri.value = file.absolutePath
    }

    fun savePlaylist(
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