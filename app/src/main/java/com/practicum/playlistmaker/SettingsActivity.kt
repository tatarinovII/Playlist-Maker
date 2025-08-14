package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolBar = findViewById<Toolbar>(R.id.toolBar)
        val userAgreementButton = findViewById<ImageView>(R.id.userAgreementButton)
        val contactSupportButton = findViewById<ImageView>(R.id.contactSupportButton)
        val shareApplicationButton = findViewById<ImageView>(R.id.shareApplicationButton)

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
    }
}