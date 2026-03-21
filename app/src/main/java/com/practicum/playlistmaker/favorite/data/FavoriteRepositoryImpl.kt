package com.practicum.playlistmaker.favorite.data

import com.practicum.playlistmaker.favorite.data.db.AppDatabase
import com.practicum.playlistmaker.favorite.data.mappers.FavoriteDbConvertor
import com.practicum.playlistmaker.favorite.domain.FavoriteRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val database: AppDatabase, private val mapper: FavoriteDbConvertor
) : FavoriteRepository {
    private val dao = database.favoriteDao()

    override suspend fun addTrack(track: Track) {
        dao.insertTrack(mapper.map(track))
    }

    override suspend fun removeTrack(track: Track) {
        dao.deleteTrack(mapper.map(track))
    }

    override suspend fun getAllTracks(): Flow<List<Track>> = flow {
        val tracks = dao.getAllTracks()
        emit(tracks.map { mapper.map(it) })
    }
}