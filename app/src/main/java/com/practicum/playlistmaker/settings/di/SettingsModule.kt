package com.practicum.playlistmaker.settings.di

import android.content.Context
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.settings.domain.ThemeSwitcher
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


val settingsModule = module {

    single(named("settingsPrefs")) {
        androidContext()
            .getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
    }

    single<ThemeSwitcher> {
        androidContext().applicationContext as App
    }

}