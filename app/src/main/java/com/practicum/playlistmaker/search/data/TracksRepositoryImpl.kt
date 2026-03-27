package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.favorite.data.db.FavoriteDao
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient, private val favoriteDao: FavoriteDao
) : TrackRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                with(response as TrackSearchResponse) {
                    val data = results.map {
                        Track(
                            trackId = it.trackId,
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                            artworkUrl100 = it.artworkUrl100,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl
                        )
                    }
                    val favoriteTracksIds = favoriteDao.getAllIds()
                    val newData = data.map {
                        if (it.trackId in favoriteTracksIds) {
                            it.copy(isFavorite = true)
                        } else it
                    }
                    emit(Resource.Success(newData))
                }
            }

            else -> {
                emit(Resource.Error("server error"))
            }
        }
    }
}