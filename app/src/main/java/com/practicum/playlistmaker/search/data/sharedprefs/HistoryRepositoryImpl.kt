package com.practicum.playlistmaker.search.data.sharedprefs

import com.practicum.playlistmaker.search.data.StorageClient
import com.practicum.playlistmaker.utils.Resource
import com.practicum.playlistmaker.search.domain.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

class HistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<Track>>
) : HistoryRepository {

    override fun saveTrack(track: Track): Boolean {
        val list = storage.getData() ?: ArrayList<Track>()
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

        storage.storeData(list)
        return true
    }

    override fun getHistory(): Resource<List<Track>> {
        val data =
            storage.getData() ?: return Resource.Error("Ошибка получения данных в getHistory()")
        return Resource.Success(data)
    }

    override fun clearHistory() {
        storage.storeData(ArrayList<Track>())
    }
}