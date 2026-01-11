package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioplayerBinding
    private lateinit var viewModel: PlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, PlayerViewModel.getFactory(
                intent.getParcelableExtra<Track>("TRACK")
            )
        ).get(PlayerViewModel::class.java)

        viewModel.preparePlayer()
        val track = viewModel.getTrack()
        if (track != null) {
            try {
                Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                    .fitCenter().placeholder(R.drawable.ic_placeholder_album)
                    .into(findViewById<ImageView>(R.id.ivAlbumPhoto))
            } catch (e: Exception) {
                findViewById<ImageView>(R.id.ivAlbumPhoto).setImageResource(R.drawable.ic_placeholder_album)
            }
            binding.trackName.text = track.trackName
            binding.tvTrackArtistName.text = track.artistName
            binding.tvTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            binding.tvDuration.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            binding.tvGenre.text = track.primaryGenreName
            binding.tvCountry.text = track.country

            if (track.collectionName != null) {
                binding.tvAlbum.text = track.collectionName
                binding.tvAlbum.isVisible = true
                binding.TextViewAlbum.isVisible = true
            } else {
                binding.tvAlbum.isVisible = false
                binding.TextViewAlbum.isVisible = false
            }
            if (track.releaseDate != null) {
                binding.tvYear.text = track.releaseDate!!.split('-')[0]
                binding.tvYear.isVisible = true
                binding.TextViewYear.isVisible = true
            } else {
                binding.tvYear.isVisible = false
                binding.TextViewYear.isVisible = false
            }
        } else {
            finish()
        }

        findViewById<ImageButton>(R.id.btnBackFromPlayer).setOnClickListener {
            finish()
        }

        binding.ibPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.observeTimeProgress().observe(this) {
            binding.tvTrackTime.text = it
        }

        viewModel.observePlayerState().observe(this) {
            when (it) {
                STATE_PLAYING -> {
                    binding.ibPlay.setImageResource(R.drawable.ic_pause_button)
                }

                else -> {
                    binding.ibPlay.setImageResource(R.drawable.ic_button_play)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroyPlayer()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}