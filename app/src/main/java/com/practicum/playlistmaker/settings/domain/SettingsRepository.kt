package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.models.ThemeSettings

interface SettingsRepository {

    fun getThemeSettings(): ThemeSettings
    fun updatedThemeSettings(settings: ThemeSettings)

}