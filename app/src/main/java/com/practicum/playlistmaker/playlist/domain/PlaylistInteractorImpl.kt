package com.practicum.playlistmaker.playlist.domain

import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist)
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun addNewTrackToPlaylist(
        trackId: Long,
        playlist: Playlist
    ) {
        val currentListOfTrackIds = playlist.tracksIds.toMutableList()
        currentListOfTrackIds.add(trackId)
        playlistRepository.updatePlaylist(playlist.copy(tracksIds = currentListOfTrackIds))
    }
}