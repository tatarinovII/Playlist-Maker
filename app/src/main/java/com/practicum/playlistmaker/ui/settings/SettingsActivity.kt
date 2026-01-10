package com.practicum.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.SettingsInteractor

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolBar = findViewById<Toolbar>(R.id.toolBar)
        val userAgreementButton = findViewById<TextView>(R.id.userAgreementButton)
        val contactSupportButton = findViewById<TextView>(R.id.contactSupportButton)
        val shareApplicationButton = findViewById<TextView>(R.id.shareApplicationButton)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        settingsInteractor = Creator.provideSettingsInteractor(applicationContext)

        if (settingsInteractor.getThemeSettings().darkTheme) {
            themeSwitcher.setChecked(true)
        }

        toolBar.setNavigationOnClickListener {
            finish()
        }

        contactSupportButton.setOnClickListener {
            val message = getString(R.string.email_message)
            val subject = getString(R.string.email_subject)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ilya.tatarinov77@gmail.com"))
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            startActivity(intent)
        }

        userAgreementButton.setOnClickListener {
            val url = getString(R.string.user_agreement_url)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        shareApplicationButton.setOnClickListener {
            val message = getString(R.string.share_url)
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intent)
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }
}