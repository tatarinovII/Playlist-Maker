package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.favorite.ui.FavoriteViewModel
import com.practicum.playlistmaker.library.ui.playlist.PlaylistViewModel
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.playlist.ui.playlist_page.PlaylistPageFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFragment : Fragment() {

    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private val playlistViewModel: PlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                LibraryScreen(
                    playlistViewModel = playlistViewModel,
                    favoriteViewModel = favoriteViewModel,
                    onTrackClicked = {
                        if (findNavController().currentDestination?.id == R.id.libraryFragment) {
                            findNavController().navigate(
                                R.id.action_libraryFragment_to_playerFragment,
                                PlayerFragment.createArgs(it)
                            )
                        }
                    },
                    onItemPlaylistClicked = {
                        findNavController().navigate(
                            R.id.action_libraryFragment_to_playlistPageFragment,
                            PlaylistPageFragment.createArgs(it)
                        )
                    },
                    onButtonCreatePlaylistClicked = {
                        findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        playlistViewModel.getAllPlaylists()
        favoriteViewModel.loadFavorites()
    }
}