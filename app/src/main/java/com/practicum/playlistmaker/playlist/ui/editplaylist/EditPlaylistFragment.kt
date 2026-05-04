package com.practicum.playlistmaker.playlist.ui.editplaylist

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.ui.newplaylist.NewPlaylistFragment
import org.koin.android.ext.android.inject
import java.io.File

class EditPlaylistFragment : NewPlaylistFragment() {

    private var isPhotoChanged: Boolean = false
    override val viewModel: EditPlayListViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = arguments?.getLong(ARGS) ?: return
        viewModel.loadPlaylist(playlistId)

        viewModel.observeData().observe(viewLifecycleOwner) {
            binding.etName.setText(it.name)
            binding.etDescription.setText(it.description)
            binding.tbNewPlaylist.title = getString(R.string.edit)
            binding.btnCreate.text = getString(R.string.save)
            if (it.uri.isNotEmpty()) {
                val file: File = File(it.uri)
                binding.ivPhotoPicker.setImageURI(Uri.fromFile(file))
            } else binding.ivPhotoPicker.setImageResource(R.drawable.ic_placeholder_album)
            if (it.uri.isNotEmpty()) binding.ivPhotoPickerIcon.isVisible = false
        }

        binding.btnCreate.setOnClickListener {
            viewModel.savePlaylist(binding.etName.text.toString(), binding.etDescription.text.toString())
            findNavController().navigateUp()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    isPhotoChanged = true
                    binding.ivPhotoPicker.setImageURI(uri)
                    binding.ivPhotoPicker.background = null
                    viewModel.saveImageToPrivateStorage(uri)
                }
            }

        binding.ivPhotoPicker.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun tryBack(photoSelected: Boolean) {
        findNavController().navigateUp()
    }
    companion object {
        const val ARGS = "playlist_edit"
        fun createArgs(playlistId: Long): Bundle = bundleOf(ARGS to playlistId)
    }
}