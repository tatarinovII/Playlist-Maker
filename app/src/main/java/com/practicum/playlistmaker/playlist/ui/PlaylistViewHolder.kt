package com.practicum.playlistmaker.playlist.ui

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.Playlist
import java.io.File

class PlaylistViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    private val tvName: TextView = item.findViewById(R.id.tvPlaylistName)
    private val tvTracksCount: TextView = item.findViewById(R.id.tvTracksCount)
    private val ivAlbumPhoto: ImageView = item.findViewById(R.id.ivAlbumImage)

    fun bind(playlist: Playlist) {
        tvName.text = playlist.name
        tvTracksCount.text = "${playlist.tracksIds.size} треков"
        if (playlist.uri.isEmpty()) ivAlbumPhoto.setImageResource(R.drawable.ic_placeholder_album)
        else {
            val file = File(playlist.uri)
            if (file.exists()) {
                ivAlbumPhoto.setImageURI(Uri.fromFile(file))
            } else ivAlbumPhoto.setImageResource(R.drawable.ic_placeholder_album)
        }
    }

}