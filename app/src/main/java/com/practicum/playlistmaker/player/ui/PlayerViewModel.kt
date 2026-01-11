package com.practicum.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track?
) : ViewModel() {
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            updateCurrentTime()
            handler.postDelayed(this, 300L)
        }
    }
    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer = MediaPlayer()
    private var playerStateLiveData = MutableLiveData(STATE_DEFAULT)
    fun observePlayerState(): LiveData<Int> = playerStateLiveData

    private var timeProgressLiveData = MutableLiveData("00:00")
    fun observeTimeProgress(): LiveData<String> = timeProgressLiveData

    fun preparePlayer() {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.value = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.value = STATE_PREPARED
            handler.removeCallbacks(updateTimeRunnable)
        }
    }

    fun updateCurrentTime() {
        if (mediaPlayer.isPlaying) {
            timeProgressLiveData.value =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        handler.post(updateTimeRunnable)
        playerStateLiveData.value = STATE_PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerStateLiveData.value = STATE_PAUSED
        handler.removeCallbacks(updateTimeRunnable)
    }

    fun playbackControl() {
        when (playerStateLiveData.value) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    fun onDestroyPlayer() {
        mediaPlayer.release()
        handler.removeCallbacks(updateTimeRunnable)
    }

    fun getTrack(): Track? = track

    companion object {
        fun getFactory(track: Track?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(track)
            }
        }

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}