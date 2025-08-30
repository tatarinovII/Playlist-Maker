package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SongViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    private val image : ImageView = item.findViewById(R.id.ivAlbum)
    private val tvSongName : TextView = item.findViewById(R.id.tvSongName)
    private val tvArtistName : TextView = item.findViewById(R.id.tvArtistName)
    private val tvSongDuration : TextView = item.findViewById(R.id.tvSongDuration)

    fun bind(track: Track) {
        tvSongName.text = track.trackName
        tvArtistName.text = track.artistName
        tvSongDuration.text = track.trackTime
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(2))
            .fitCenter()
            .placeholder(R.drawable.ic_launcher_background)
            .into(image)
    }
}