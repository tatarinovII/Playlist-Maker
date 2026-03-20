package com.practicum.playlistmaker.favorite.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class FavoriteTrackEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "track_name") val trackName: String,
    @ColumnInfo(name = "artist_name") val artistName: String,
    @ColumnInfo(name = "time_in_mills") val trackTimeMillis: Int,
    @ColumnInfo(name = "album_photo") val artworkUrl100: String,
    @ColumnInfo(name = "album_title") val collectionName: String? = null,
    @ColumnInfo(name = "release_date") val releaseDate: String? = null,
    @ColumnInfo(name = "genre") val primaryGenreName: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "media_url") val previewUrl: String
)
