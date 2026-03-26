package com.practicum.playlistmaker.player.ui

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.Playlist
import java.io.File

class PlayerViewHolder(
    item: View,
    private val onItemClick: (Playlist) -> Unit
): RecyclerView.ViewHolder(item) {

    private val ivAlbumImage: ImageView = item.findViewById(R.id.ivAlbumImage)
    private val tvAlbumName: TextView = item.findViewById(R.id.tvAlbumName)
    private val tvTracksCount: TextView = item.findViewById(R.id.tvTracksCount)
    private val llTrack: LinearLayout = item.findViewById(R.id.llTrack)

    fun bind(playlist: Playlist) {
        if (playlist.uri.isEmpty()) ivAlbumImage.setImageResource(R.drawable.ic_placeholder_album)
        else {
            val file = File(playlist.uri)
            if (file.exists()) {
                ivAlbumImage.setImageURI(Uri.fromFile(file))
            } else ivAlbumImage.setImageResource(R.drawable.ic_placeholder_album)
        }
        tvAlbumName.text = playlist.name
        tvTracksCount.text = "${playlist.tracksIds.size} треков"
        llTrack.setOnClickListener {
            onItemClick(playlist)
        }

    }

}