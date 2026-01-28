package com.practicum.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.models.MediaPlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {
    private lateinit var binding: FragmentPlayerBinding

    private val track: Track? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ARGS_PLAYER, Track::class.java)
        } else {
            requireArguments().getParcelable(ARGS_PLAYER)
        }
    }
    private lateinit var viewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm: PlayerViewModel by viewModel() {
            parametersOf(track)
        }

        viewModel = vm

        if (track != null) {
            try {
                Glide.with(this).load(track!!.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                    .fitCenter().placeholder(R.drawable.ic_placeholder_album)
                    .into(binding.ivAlbumPhoto)
            } catch (e: Exception) {
                binding.ivAlbumPhoto.setImageResource(R.drawable.ic_placeholder_album)
            }
            binding.trackName.text = track!!.trackName
            binding.tvTrackArtistName.text = track!!.artistName
            binding.tvTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            binding.tvDuration.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track!!.trackTimeMillis)
            binding.tvGenre.text = track!!.primaryGenreName
            binding.tvCountry.text = track!!.country

            if (track!!.collectionName != null) {
                binding.tvAlbum.text = track!!.collectionName
                binding.tvAlbum.isVisible = true
                binding.TextViewAlbum.isVisible = true
            } else {
                binding.tvAlbum.isVisible = false
                binding.TextViewAlbum.isVisible = false
            }
            if (track!!.releaseDate != null) {
                binding.tvYear.text = track!!.releaseDate!!.split('-')[0]
                binding.tvYear.isVisible = true
                binding.TextViewYear.isVisible = true
            } else {
                binding.tvYear.isVisible = false
                binding.TextViewYear.isVisible = false
            }
        } else {
            findNavController().navigateUp()
        }

        binding.btnBackFromPlayer.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ibPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            binding.tvTrackTime.text = it.timeProgress
            when (it.state) {
                MediaPlayerState.STATE_PLAYING.state -> {
                    binding.ibPlay.setImageResource(R.drawable.ic_pause_button)
                }

                else -> {
                    binding.ibPlay.setImageResource(R.drawable.ic_button_play)
                }
            }
        }
    }

    companion object {
        private const val ARGS_PLAYER = "TRACK"
        fun createArgs(track: Track): Bundle = bundleOf(ARGS_PLAYER to track)
    }
}