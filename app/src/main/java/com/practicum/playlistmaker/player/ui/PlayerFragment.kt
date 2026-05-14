package com.practicum.playlistmaker.player.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.models.PlayerState
import com.practicum.playlistmaker.player.ui.rcview.PlayerAdapter
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.android.ext.android.get
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

    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            val binder = service as MusicService.MusicServiceBinder
            viewModel.setAudioPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            bindMusicService()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm: PlayerViewModel by viewModel {
            parametersOf(track)
        }

        viewModel = vm

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            bindMusicService()
        }

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
            if (track!!.isFavorite) {
                binding.ibLike.setImageResource(R.drawable.ic_liked)
            }
        } else {
            findNavController().navigateUp()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.imAddToQueue.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.isVisible = true
                        vm.loadPlaylists()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        binding.btnBackFromPlayer.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ibPlay.setOnClickListener {
            viewModel.onPlayButtonClicked()
            binding.ibPlay.changeState()
        }

        binding.rvPlaylists.layoutManager = LinearLayoutManager(requireContext())

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            binding.ibPlay.isEnabled = it.isPlayButtonEnabled
            binding.tvTrackTime.text = it.progress
            if (it.listOfPlaylists.isNotEmpty()) binding.rvPlaylists.adapter = PlayerAdapter(
                it.listOfPlaylists,
                onItemClick = {
                    if (it.tracksIds.contains(track!!.trackId)) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.track_already_added, it.name),
                            Toast.LENGTH_SHORT).show()
                        return@PlayerAdapter
                    }
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.added_to_playlist, it.name),
                        Toast.LENGTH_SHORT
                    ).show()
                    vm.addTrackToPlaylist(it)
                }
            )
            if (it.isFavorite) binding.ibLike.setImageResource(R.drawable.ic_liked) else binding.ibLike.setImageResource(R.drawable.ic_unliked)
            when (it) {

                is PlayerState.Prepared -> {
                    binding.ibPlay.setPlayImageAfterEnd()
                }

                else -> {}
            }
        }

        binding.ibLike.setOnClickListener {
            viewModel.onButtonLikeClicked()
        }

        binding.btnCreateNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }
    }

    private fun bindMusicService() {
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            putExtra("song_url", track?.previewUrl)
            putExtra("is_favorite", track?.isFavorite)
            putExtra("artist_name", track?.artistName)
            putExtra("song_name", track?.trackName)
        }
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        requireContext().unbindService(serviceConnection)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindMusicService()
    }

    override fun onPause() {
        super.onPause()
        viewModel.showNotification()
    }

    override fun onResume() {
        super.onResume()
        viewModel.hideNotification()
    }



    companion object {
        private const val ARGS_PLAYER = "TRACK"
        fun createArgs(track: Track): Bundle = bundleOf(ARGS_PLAYER to track)
    }
}