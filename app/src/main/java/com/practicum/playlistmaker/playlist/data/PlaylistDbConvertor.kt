package com.practicum.playlistmaker.playlist.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.playlist.domain.Playlist

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
}