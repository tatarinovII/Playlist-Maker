package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.ThemeSwitcher

class App : Application(), ThemeSwitcher {

    override fun onCreate() {
        super.onCreate()
        val settingsRepository = Creator.getSettingsRepository(this)
        val darkTheme = settingsRepository.getThemeSettings().darkTheme
        applyTheme(darkTheme)
    }

    override fun applyTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}