package com.practicum.playlistmaker.playlist.ui.newplaylist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

open class NewPlaylistFragment : Fragment() {

   open val viewModel: NewPlaylistViewModel by viewModel()
    protected lateinit var binding: FragmentNewPlaylistBinding
    private var photoSelected = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.isEnabled = false
        binding.tbNewPlaylist.setNavigationOnClickListener {
            tryBack(photoSelected)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                tryBack(photoSelected)
            }
        })

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotEmpty() == true) {
                    binding.btnCreate.isEnabled = true
                } else binding.btnCreate.isEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {}

        }

        binding.etName.addTextChangedListener(textWatcher)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    photoSelected = true
                    binding.ivPhotoPicker.setImageURI(uri)
                    binding.ivPhotoPicker.background = null
                    binding.ivPhotoPickerIcon.isVisible = false
                    viewModel.saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.btnCreate.setOnClickListener {
            viewModel.savePlaylist(
                name = binding.etName.text.toString(),
                description = binding.etDescription.text.toString()
            )
            Toast.makeText(
                requireContext(),
                "Плейлист ${binding.etName.text.toString()} создан!",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigateUp()
        }

        binding.ivPhotoPicker.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    protected open fun tryBack(photoSelected: Boolean) {
        if (photoSelected || binding.etName.text.isNotEmpty() || binding.etDescription.text.isNotEmpty()) {
            MaterialAlertDialogBuilder(
                requireContext(), R.style.PlaylistDialogTheme
            ).setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setPositiveButton("Завершить") { dialog, which ->
                    findNavController().navigateUp()
                }.setNegativeButton("Отмена") { dialog, which ->

                }.show()
        } else {
            findNavController().navigateUp()
        }
    }
}