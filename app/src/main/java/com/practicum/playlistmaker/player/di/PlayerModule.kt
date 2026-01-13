package com.practicum.playlistmaker.player.di

import android.media.MediaPlayer
import org.koin.dsl.module

val playerModule = module {
    factory<MediaPlayer> {
        MediaPlayer()
    }
}