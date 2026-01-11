package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.models.ThemeSettings
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun getSwitchState(): Boolean {
        return settingsInteractor.getThemeSettings().darkTheme
    }

    fun onContactSupportButtonClicked() {
        sharingInteractor.openSupport()
    }

    fun onShareButtonClicked() {
        sharingInteractor.shareApp()
    }

    fun onUserAgreementButtonClicked() {
        sharingInteractor.openTerms()
    }

    fun switchTheme(isDark: Boolean) {
        settingsInteractor.updateThemeSettings(ThemeSettings(isDark))
    }

    companion object {
        fun getFactory(
            sharingInteractor: SharingInteractor,
            settingsInteractor: SettingsInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(sharingInteractor, settingsInteractor)
            }
        }
    }
}