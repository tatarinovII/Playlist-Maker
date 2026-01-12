package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences

import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.models.ThemeSettings

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences) : SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(darkTheme = sharedPrefs.getBoolean(THEME_KEY, false))
    }

    override fun updatedThemeSettings(settings: ThemeSettings) {
        sharedPrefs.edit().putBoolean(THEME_KEY, settings.darkTheme).apply()
    }

    companion object {
        const val THEME_KEY = "theme_status_key"
    }
}