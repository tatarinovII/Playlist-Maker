package com.practicum.playlistmaker.playlist.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylist(playlist: Playlist)
}