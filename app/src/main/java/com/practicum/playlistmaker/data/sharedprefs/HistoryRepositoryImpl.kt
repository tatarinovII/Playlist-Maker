package com.practicum.playlistmaker.data.sharedprefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.api.HistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class HistoryRepositoryImpl(
    val sharedPrefs: SharedPreferences
) : HistoryRepository {

    override fun saveTrack(track: Track): Boolean {
        val list = getHistory().toMutableList()
        if (list.size < 10 && !list.contains(track)) {
            list.add(0, track)
            if (list.size >= 10) list.removeAt(list.lastIndex)
        } else if (list.size <= 10 && list.contains(track)) {
            list.remove(track)
            list.add(0, track)
        } else if (list.size >= 10 && !list.contains(track)) {
            list.removeAt(list.lastIndex)
            list.add(0, track)
        } else return false

        write(list)
        return true
    }

    override fun getHistory(): List<Track> {
        val json = sharedPrefs.getString("TRACK_LIST", null) ?: return emptyList<Track>()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    fun write(list: List<Track>) {
        val json = Gson().toJson(list)
        sharedPrefs.edit { putString("TRACK_LIST", json) }
    }

    override fun clearHistory() {
        sharedPrefs.edit { clear() }
    }
}