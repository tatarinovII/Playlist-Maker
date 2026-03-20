package com.practicum.playlistmaker.favorite.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getAllTracks(): List<FavoriteTrackEntity>

    @Delete
    suspend fun deleteTrack(track: FavoriteTrackEntity)

    @Query("SELECT id FROM favorite_tracks")
    suspend fun getAllIds(): List<Long>

}