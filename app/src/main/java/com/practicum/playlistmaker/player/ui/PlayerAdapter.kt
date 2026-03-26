package com.practicum.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.Playlist

class PlayerAdapter(
    private val list: List<Playlist>,
    private val onItemClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlayerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PlayerViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist_for_audioplayer, parent, false)
        return PlayerViewHolder(item, onItemClick)
    }

    override fun onBindViewHolder(
        holder: PlayerViewHolder, position: Int
    ) {
        holder.bind(playlist = list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}