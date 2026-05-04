package com.practicum.playlistmaker.playlist.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.playlist.data.database.models.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track WHERE id = :trackId")
    suspend fun getTrackById(trackId: Long): PlaylistTrackEntity

    @Delete
    suspend fun deleteTrack(track: PlaylistTrackEntity)
}