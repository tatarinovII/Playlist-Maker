package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.TrackIterator
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.impl.TracksIteratorImpl

object Creator {

    private fun getTracksRepository(): TrackRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksIterator(): TrackIterator {
        return TracksIteratorImpl(getTracksRepository())
    }
}