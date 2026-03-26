package com.practicum.playlistmaker.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.Playlist

class PlaylistAdapter(
    private val list: List<Playlist>
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PlaylistViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist_for_playlist_fragment, parent, false)
        return PlaylistViewHolder(item)
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder, position: Int
    ) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}