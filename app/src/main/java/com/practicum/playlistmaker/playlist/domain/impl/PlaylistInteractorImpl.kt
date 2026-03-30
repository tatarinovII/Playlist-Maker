package com.practicum.playlistmaker.playlist.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.File

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist)
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun addTrackToPlaylist(
        track: Track,
        playlist: Playlist
    ) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri): File {
        return playlistRepository.saveImageToPrivateStorage(uri)
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override suspend fun getAllTracksInPlaylist(playlist: Playlist): Flow<List<Track>> = flow {
        val data = playlist.tracksIds.map { playlistRepository.getTrackById(it) }
        emit(data)
    }

    override suspend fun deleteTrackFromPlaylist(
        playlist: Playlist,
        track: Track
    ) {
       playlistRepository.deleteTrackFromPlaylist(track, playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun editPlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

}