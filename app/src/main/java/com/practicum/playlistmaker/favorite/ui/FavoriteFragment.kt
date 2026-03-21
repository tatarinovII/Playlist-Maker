package com.practicum.playlistmaker.favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.android.ext.android.inject

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TrackAdapter { track ->
            track.isFavorite = true
            findNavController().navigate(
                R.id.action_libraryFragment_to_playerFragment, PlayerFragment.createArgs(track)
            )
        }
        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.rcView.adapter = adapter
        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteState.Default -> {
                    adapter.list = it.tracks
                    binding.tvEmptySearchOutput.isVisible = false
                    binding.rcView.isVisible = true
                }

                is FavoriteState.Empty -> {
                    binding.tvEmptySearchOutput.isVisible = true
                    binding.rcView.isVisible = false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavorites()
    }

    companion object {
        fun newInstance() = FavoriteFragment().apply {

        }
    }
}