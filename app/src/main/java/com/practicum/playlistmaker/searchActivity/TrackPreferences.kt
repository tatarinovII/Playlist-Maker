package com.practicum.playlistmaker.searchActivity

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.models.Track

class TrackPreferences(
    val sharedPrefs: SharedPreferences
) {
    fun addToSharedPrefs(newTrack: Track): Boolean {
        val list = read().toMutableList()
        if (list.size < 10 && !list.contains(newTrack)) {
            list.add(0, newTrack)
            if (list.size >= 10) list.removeAt(list.lastIndex)
        } else if (list.size <= 10 && list.contains(newTrack)) {
            list.remove(newTrack)
            list.add(0, newTrack)
        } else if (list.size >= 10 && !list.contains(newTrack)) {
            list.removeAt(list.lastIndex)
            list.add(0, newTrack)
        } else return false

        write(list)
        return true
    }

    fun read(): List<Track> {
        val json = sharedPrefs.getString("TRACK_LIST", null) ?: return emptyList<Track>()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    fun write(list: List<Track>) {
        val json = Gson().toJson(list)
        sharedPrefs.edit { putString("TRACK_LIST", json) }
    }

    fun clear() {
        sharedPrefs.edit { clear() }
    }
}