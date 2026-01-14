package com.practicum.playlistmaker.settings.domain.impl


import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.ThemeSwitcher
import com.practicum.playlistmaker.settings.models.ThemeSettings

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
    private val themeSwitcher: ThemeSwitcher
) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSettings(settings: ThemeSettings) {
        if (getThemeSettings().darkTheme == settings.darkTheme) return
        settingsRepository.updatedThemeSettings(settings)
        themeSwitcher.applyTheme(settings.darkTheme)
    }
}