package com.practicum.playlistmaker.favorite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.playlist.data.database.dao.PlaylistDao
import com.practicum.playlistmaker.playlist.data.database.dao.PlaylistTrackDao
import com.practicum.playlistmaker.playlist.data.database.models.PlaylistEntity
import com.practicum.playlistmaker.playlist.data.database.models.PlaylistTrackEntity

@Database(
    entities = [FavoriteTrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistTrackDao(): PlaylistTrackDao

}