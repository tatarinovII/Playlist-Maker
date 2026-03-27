package com.practicum.playlistmaker.playlist.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.practicum.playlistmaker.playlist.data.database.PlaylistDbConvertor
import com.practicum.playlistmaker.playlist.data.database.dao.PlaylistDao
import com.practicum.playlistmaker.playlist.data.database.dao.PlaylistTrackDao
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlaylistRepositoryImpl(
    private val converter: PlaylistDbConvertor,
    private val playlistDao: PlaylistDao,
    private val trackDao: PlaylistTrackDao,
    private val context: Context
): PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(converter.map(playlist))
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val list = playlistDao.getAllTracks()
        emit(list.map { converter.map(it) })
    }

    override suspend fun addTrackToPlaylist(
        track: Track,
        playlist: Playlist
    ) {
        val updatedIds = playlist.tracksIds.toMutableList().apply { add(track.trackId) }
        val updatedPlaylist = playlist.copy(
            tracksIds = updatedIds
        )
        playlistDao.updatePlaylist(converter.map(updatedPlaylist))

        trackDao.insertTrack(converter.map(track))
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri): File {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${UUID.randomUUID()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }
}