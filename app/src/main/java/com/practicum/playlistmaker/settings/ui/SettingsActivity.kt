package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeSwitchState().observe(this) {
            if (it) {
                binding.themeSwitcher.setChecked(true)
            }
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