package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.models.ThemeSettings


interface SettingsInteractor {

    fun getThemeSettings(): ThemeSettings

    fun updateThemeSettings(settings: ThemeSettings)

}