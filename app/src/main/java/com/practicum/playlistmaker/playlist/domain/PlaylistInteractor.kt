package com.practicum.playlistmaker.playlist.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addNewTrackToPlaylist(trackId: Long, playlist: Playlist)

}