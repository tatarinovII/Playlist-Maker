package com.practicum.playlistmaker.playlist.ui.rcview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.models.Playlist

class PlaylistAdapter(
    private val list: List<Playlist>,
    private val onItemClicked: (Long) -> Unit
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PlaylistViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist_for_playlist_fragment, parent, false)
        return PlaylistViewHolder(item, onItemClicked)
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