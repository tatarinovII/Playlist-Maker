package com.practicum.playlistmaker.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.api.SettingsRepository
import com.practicum.playlistmaker.domain.models.ThemeSettings

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