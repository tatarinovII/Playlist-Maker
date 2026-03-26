package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favorite.data.FavoriteRepositoryImpl
import com.practicum.playlistmaker.favorite.domain.FavoriteRepository
import com.practicum.playlistmaker.playlist.data.PlaylistRepositoryImpl
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.sharedprefs.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.HistoryRepository
import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.data.SharingRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    factory<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    factory<TrackRepository> {
        TracksRepositoryImpl(get(), get())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get(named("settingsPrefs")))
    }

    factory<SharingRepository> {
        SharingRepositoryImpl(androidContext())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }
}