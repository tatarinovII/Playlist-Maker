package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TrackIterator
import com.practicum.playlistmaker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TracksIteratorImpl(private val repository: TrackRepository) : TrackIterator {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        expression: String,
        consumer: TrackIterator.TracksConsumer
    ) {
        executor.execute {
            val tracks = repository.searchTracks(expression)
            consumer.consume(tracks)
        }
    }

}