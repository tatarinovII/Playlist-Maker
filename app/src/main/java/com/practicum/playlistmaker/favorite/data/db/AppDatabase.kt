package com.practicum.playlistmaker.favorite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.playlist.data.PlaylistDao
import com.practicum.playlistmaker.playlist.data.PlaylistEntity

@Database(entities = [FavoriteTrackEntity::class, PlaylistEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
    abstract fun playlistDao(): PlaylistDao

}