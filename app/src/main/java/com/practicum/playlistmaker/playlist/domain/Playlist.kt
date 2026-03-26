package com.practicum.playlistmaker.playlist.domain

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val uri: String = "",
    val tracksIds: List<Long> = emptyList<Long>(),
    //val tracksCount: Int = 0
)
