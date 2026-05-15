package com.practicum.playlistmaker.player.ui

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.models.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MusicService : Service(), AudioPlayerControl {

    private var mediaPlayer: MediaPlayer? = null
    private var songUrl: String? = ""
    private var isFavorite = false
    private var songName: String? = ""
    private var artistName: String? = ""
    private val binder = MusicServiceBinder()
    private var timerJob: Job? = null
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default(false))
    private val playerStateFlow = _playerState.asStateFlow()

    override fun onBind(intent: Intent?): IBinder {
        songUrl = intent?.getStringExtra("song_url")
        isFavorite = intent?.getBooleanExtra("is_favorite", false) ?: false
        songName = intent?.getStringExtra("song_name")
        artistName = intent?.getStringExtra("artist_name")
        mediaPlayer = MediaPlayer()

        initMediaPlayer()
        createNotificationChannel()

        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayer?.isPlaying == true) {
                _playerState.value = PlayerState.Playing(getCurrentPosition(), isFavorite)
                delay(300L)
            }
        }
    }

    private fun getCurrentPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(
            mediaPlayer?.currentPosition ?: 0
        )
    }

    private fun initMediaPlayer() {
        if (songUrl!!.isEmpty()) return

        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared(isFavorite)
        }
        mediaPlayer?.setOnCompletionListener {
            timerJob?.cancel()
            _playerState.value = PlayerState.Prepared(isFavorite)
            hideNotification()
        }
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerStateFlow
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(getCurrentPosition(), isFavorite)
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(getCurrentPosition(), isFavorite)
    }

    override fun showNotification() {
        if (mediaPlayer?.isPlaying == false) return
        if (!checkPermission()) return
        ServiceCompat.startForeground(
            this,
            100,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun hideNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun releasePlayer() {
        mediaPlayer?.stop()
        _playerState.value = PlayerState.Default(isFavorite)
        hideNotification()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Music Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Playlist Maker")
            .setContentText("$artistName - $songName")
            .setSmallIcon(R.drawable.ic_liked)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    private companion object {
        const val NOTIFICATION_CHANNEL_ID = "my_channel_id"
    }

    private fun checkPermission() : Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

}