package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.data.network.TrackSearchRequest
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.api.TrackRepository

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map {
                Track(
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
        } else {
            return emptyList()
        }
    }
}