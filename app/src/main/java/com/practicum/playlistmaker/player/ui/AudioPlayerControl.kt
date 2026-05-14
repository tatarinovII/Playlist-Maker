package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.player.models.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun showNotification()
    fun hideNotification()
}