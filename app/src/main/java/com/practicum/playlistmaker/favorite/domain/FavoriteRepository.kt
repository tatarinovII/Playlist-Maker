package com.practicum.playlistmaker.favorite.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addTrack(track: Track)

    suspend fun removeTrack(track: Track)

    suspend fun getAllTracks(): Flow<List<Track>>

}