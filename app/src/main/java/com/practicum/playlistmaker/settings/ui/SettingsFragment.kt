package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeSwitchState().observe(viewLifecycleOwner) {
            if (binding.themeSwitcher.isChecked != it) {
                binding.themeSwitcher.isChecked = it
            }
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