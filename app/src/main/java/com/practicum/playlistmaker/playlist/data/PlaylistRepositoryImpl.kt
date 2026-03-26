package com.practicum.playlistmaker.playlist.data

import com.practicum.playlistmaker.favorite.data.db.AppDatabase
import com.practicum.playlistmaker.playlist.domain.Playlist
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase, private val converter: PlaylistDbConvertor
): PlaylistRepository {

    private val dao = appDatabase.playlistDao()

    override suspend fun createPlaylist(playlist: Playlist) {
        dao.insertPlaylist(converter.map(playlist))
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val list = dao.getAllTracks()
        emit(list.map { converter.map(it) })
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        dao.updatePlaylist(converter.map(playlist))
    }
}