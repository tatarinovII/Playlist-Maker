package com.practicum.playlistmaker.sharing.data

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.SharingRepository

class SharingRepositoryImpl(private val context: Context): SharingRepository {
    override fun getShareAppLink(): String {
        return context.getString(R.string.share_url)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            email = "ilya.tatarinov@gmail.com",
            subject = context.getString(R.string.email_subject),
            message = context.getString(R.string.email_message)
        )
    }

    override fun getTermsLink(): String {
        return context.getString(R.string.user_agreement_url)
    }
}