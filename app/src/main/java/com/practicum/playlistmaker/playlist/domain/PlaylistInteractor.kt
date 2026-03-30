package com.practicum.playlistmaker.playlist.domain

import android.net.Uri
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PlaylistInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    suspend fun saveImageToPrivateStorage(uri: Uri): File

    suspend fun getPlaylistById(playlistId: Long): Playlist

    suspend fun getAllTracksInPlaylist(playlist: Playlist): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun editPlaylist(playlist: Playlist)
}