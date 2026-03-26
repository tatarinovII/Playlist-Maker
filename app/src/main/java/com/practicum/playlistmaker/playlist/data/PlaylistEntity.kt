package com.practicum.playlistmaker.playlist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val description: String= "",
    val fileName: String = "",
    val tracksIds: String = "",
    val tracksCount: Int = 0
)
