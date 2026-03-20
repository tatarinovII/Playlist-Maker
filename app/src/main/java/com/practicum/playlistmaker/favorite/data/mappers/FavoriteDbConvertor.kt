package com.practicum.playlistmaker.favorite.data.mappers

import com.practicum.playlistmaker.favorite.data.db.FavoriteTrackEntity
import com.practicum.playlistmaker.search.domain.models.Track

class FavoriteDbConvertor() {

    fun map(entity: FavoriteTrackEntity): Track {
        return Track(
            trackName = entity.trackName,
            artistName = entity.artistName,
            trackTimeMillis = entity.trackTimeMillis,
            artworkUrl100 = entity.artworkUrl100,
            collectionName = entity.collectionName,
            releaseDate = entity.releaseDate,
            primaryGenreName = entity.primaryGenreName,
            country = entity.country,
            previewUrl = entity.previewUrl,
            trackId = entity.id
        )
    }

    fun map(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            id = track.trackId
        )
    }
}