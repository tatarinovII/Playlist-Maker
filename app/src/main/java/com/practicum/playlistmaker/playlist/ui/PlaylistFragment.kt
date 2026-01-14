package com.practicum.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
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
    }

    companion object {
        fun newInstance() = PlaylistFragment().apply {

        }
    }
}