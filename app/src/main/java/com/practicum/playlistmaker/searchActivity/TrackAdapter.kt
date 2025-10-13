package com.practicum.playlistmaker.searchActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.models.Track

class TrackAdapter(private val onItemClick: (Track) -> Unit) :
    RecyclerView.Adapter<TrackViewHolder>() {
    lateinit var list: List<Track>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song_info, parent, false)
        return TrackViewHolder(view, onItemClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(list[position])
    }
}