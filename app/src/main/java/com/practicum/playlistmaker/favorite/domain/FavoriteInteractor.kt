package com.practicum.playlistmaker.favorite.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    suspend fun getAllTracks(): Flow<List<Track>>
}