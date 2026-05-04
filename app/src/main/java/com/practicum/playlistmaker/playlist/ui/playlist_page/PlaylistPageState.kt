package com.practicum.playlistmaker.playlist.ui.playlist_page

import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

sealed class PlaylistPageState(val playlist: Playlist, val listOfTracks: List<Track>) {

    class Default(playlist: Playlist, list: List<Track>): PlaylistPageState(playlist, list)

}