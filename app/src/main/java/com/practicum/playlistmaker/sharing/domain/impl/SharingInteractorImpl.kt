package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.SharingRepository
import com.practicum.playlistmaker.sharing.domain.models.EmailData

class SharingInteractorImpl (
    private val externalNavigator: ExternalNavigator,
    private val sharingRepository: SharingRepository
): SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return sharingRepository.getShareAppLink()
    }

    private fun getSupportEmailData(): EmailData {
        return sharingRepository.getSupportEmailData()
    }

    private fun getTermsLink(): String {
        return sharingRepository.getTermsLink()
    }
}