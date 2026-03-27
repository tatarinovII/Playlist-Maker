package com.practicum.playlistmaker.playlist.data.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.playlist.data.database.models.PlaylistEntity
import com.practicum.playlistmaker.playlist.data.database.models.PlaylistTrackEntity
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

class PlaylistDbConvertor(
    private val gson: Gson
) {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            name = playlist.name,
            description = playlist.description,
            fileName = playlist.uri,
            tracksIds = gson.toJson(playlist.tracksIds),
            id = playlist.id
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        val type = object : TypeToken<List<Long>>() {}.type
        val tracksIds: List<Long> = gson.fromJson(playlist.tracksIds, type) ?: emptyList()
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            uri = playlist.fileName,
            tracksIds = tracksIds
        )
    }

    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            id = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

    fun map(track: PlaylistTrackEntity): Track {
        return Track(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            trackId = track.id
        )
    }
}