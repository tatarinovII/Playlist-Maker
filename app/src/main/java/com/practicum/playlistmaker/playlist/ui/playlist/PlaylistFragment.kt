package com.practicum.playlistmaker.playlist.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.playlist.ui.playlist_page.PlaylistPageFragment
import com.practicum.playlistmaker.playlist.ui.rcview.PlaylistAdapter
import org.koin.android.ext.android.inject

class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel: PlaylistViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistState.Default -> {
                    binding.rvPlaylists.adapter = PlaylistAdapter(
                        it.list,
                        onItemClicked = { playlistId ->
                            findNavController().navigate(R.id.action_libraryFragment_to_playlistPageFragment,
                                PlaylistPageFragment.createArgs(playlistId))
                        },
                    )
                    binding.rvPlaylists.isVisible = true
                    binding.tvEmptyPlaylistPage.isVisible = false
                }
                is PlaylistState.Empty -> {
                    binding.rvPlaylists.isVisible = false
                    binding.tvEmptyPlaylistPage.isVisible = true
                }
            }
        }

        binding.btnCreateNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_libraryFragment_to_newPlaylistFragment
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllPlaylists()
    }

    companion object {
        fun newInstance() = PlaylistFragment().apply {

        }
    }
}