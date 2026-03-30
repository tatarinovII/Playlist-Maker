package com.practicum.playlistmaker.playlist.ui.playlist_page

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.R.drawable
import com.practicum.playlistmaker.R.plurals
import com.practicum.playlistmaker.databinding.FragmentPlaylistPageBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.playlist.ui.editplaylist.EditPlaylistFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistPageFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistPageBinding

    private val playlistId: Long? by lazy {
        requireArguments().getLong(ARGS_PLAYLIST)
    }

    private lateinit var viewModel: PlaylistPageViewModel
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm: PlaylistPageViewModel by viewModel {
            parametersOf(playlistId)
        }
        viewModel = vm

        viewModel.observeState().observe(viewLifecycleOwner) {
            bindData(it.playlist, it.listOfTracks)
        }

        setUpClickListeners()


        adapter = TrackAdapter(onItemClick = {
            findNavController().navigate(
                R.id.action_playlistPageFragment_to_playerFragment, PlayerFragment.createArgs(it)
            )
        }, onItemLongClicked = {
            MaterialAlertDialogBuilder(
                requireContext(), R.style.PlaylistDialogTheme
            ).setTitle(getString(R.string.want_to_delet_track))
                .setPositiveButton("Да") { dialog, which ->
                    viewModel.deleteTrack(it)
                }.setNegativeButton("Нет") { dialog, which ->
                }.show()
        })

        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.rcView.adapter = adapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })


    }

    private fun bindData(playlist: Playlist, listOfTracks: List<Track>) {
        binding.tvPlaylistName.text = playlist.name
        binding.tvPlaylistDescription.text = playlist.description
        var durationInMills = 0
        listOfTracks.map { durationInMills += it.trackTimeMillis }
        binding.tvPlaylistAllTracksDuration.text = resources.getQuantityString(
            plurals.mins, SimpleDateFormat(
                "mm", Locale.getDefault()
            ).format(durationInMills).toInt(), SimpleDateFormat(
                "mm", Locale.getDefault()
            ).format(durationInMills).toInt()
        )
        val tracksCount =
            resources.getQuantityString(plurals.tracks_count, listOfTracks.size, listOfTracks.size)
        binding.tvTracksCount.text = tracksCount
        val file = File(playlist.uri)
        if (file.exists()) {
            binding.ivPlaylistImage.setImageURI(Uri.fromFile(file))
            binding.ivPlaylistImageMenu.setImageURI(Uri.fromFile(file))
        } else  {
            binding.ivPlaylistImage.setImageResource(drawable.ic_placeholder_album)
            binding.ivPlaylistImageMenu.setImageResource(drawable.ic_placeholder_album)
        }
        adapter.list = listOfTracks
        binding.tvPlaylistNameInMenu.text = playlist.name
        binding.tvTracksCountInMenu.text = tracksCount
    }

    private fun setUpClickListeners() {
        val menuBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        menuBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        binding.ivDots.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.overlay.isVisible = true
        }

        binding.ivShare.setOnClickListener {
            viewModel.sharePlayList()
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        binding.ll.post {
            val location = IntArray(2)
            binding.ll.getLocationInWindow(location)
            val dotsBottom = location[1] + binding.ll.height

            val screenHeight = requireContext().resources.displayMetrics.heightPixels
            val peekHeight = screenHeight - dotsBottom

            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            bottomSheetBehavior.peekHeight = peekHeight
        }

        binding.tvSharePlaylist.setOnClickListener {
            viewModel.sharePlayList()
        }

        binding.tvDeletePlaylist.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.PlaylistDialogTheme)
                .setTitle(getString(R.string.wanna_delete_playlist, binding.tvPlaylistName.text))
                .setNegativeButton(getString(R.string.no)) {dialog, which ->

                }
                .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    viewModel.deletePlaylist()
                    findNavController().navigateUp()
                }.show()
        }

        binding.tvEditPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playlistPageFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlistId!!))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlaylistData(playlistId!!)
    }


    companion object {
        private const val ARGS_PLAYLIST = "PLAYLIST"
        fun createArgs(playlistId: Long): Bundle = bundleOf(ARGS_PLAYLIST to playlistId)
    }
}
