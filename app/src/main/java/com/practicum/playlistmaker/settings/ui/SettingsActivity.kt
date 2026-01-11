package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getFactory(
                Creator.provideSharingInteractor(this),
                Creator.provideSettingsInteractor(this)
            )
        ).get(SettingsViewModel::class.java)

        if (viewModel.getSwitchState()) {
            binding.themeSwitcher.setChecked(true)
        }

        binding.toolBar.setNavigationOnClickListener {
            finish()
        }

        binding.contactSupportButton.setOnClickListener {
            viewModel.onContactSupportButtonClicked()
        }

        binding.userAgreementButton.setOnClickListener {
            viewModel.onUserAgreementButtonClicked()
        }

        binding.shareApplicationButton.setOnClickListener {
            viewModel.onShareButtonClicked()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }
    }
}