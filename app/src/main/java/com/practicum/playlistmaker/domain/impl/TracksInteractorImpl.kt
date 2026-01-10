package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        expression: String, consumer: TrackInteractor.TracksConsumer
    ) {
        executor.execute {
            val tracks = repository.searchTracks(expression)
            consumer.consume(tracks)
        }
    }

}