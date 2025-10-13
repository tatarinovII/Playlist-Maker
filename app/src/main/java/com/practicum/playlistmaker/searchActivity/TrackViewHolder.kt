package com.practicum.playlistmaker.searchActivity

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(item: View, private val onItemClick: (Track) -> Unit) :
    RecyclerView.ViewHolder(item) {

    private val image: ImageView = item.findViewById(R.id.ivAlbum)
    private val tvSongName: TextView = item.findViewById(R.id.tvSongName)
    private val tvArtistName: TextView = item.findViewById(R.id.tvArtistName)
    private val tvSongDuration: TextView = item.findViewById(R.id.tvSongDuration)
    private val llTrack: LinearLayout = item.findViewById(R.id.llTrack)

    fun bind(track: Track) {
        tvSongName.text = track.trackName
        tvArtistName.text = track.artistName
        tvSongDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(293000L)
        Glide.with(itemView.context).load(track.artworkUrl100)
            .transform(RoundedCorners(dpToPx(2f, itemView.context))).fitCenter()
            .placeholder(R.drawable.ic_placeholder_album).into(image)
        llTrack.setOnClickListener {
            onItemClick(track)
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        ).toInt()
    }
}