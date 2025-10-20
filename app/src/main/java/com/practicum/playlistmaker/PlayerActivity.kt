package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var ivAlbumPhoto: ImageView
    private lateinit var tvTrackName: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var tvTrackCurrentTime: TextView
    private lateinit var tvDuration: TextView
    private lateinit var tvAlbumTitle: TextView
    private lateinit var tvAlbum: TextView
    private lateinit var tvYearTitle: TextView
    private lateinit var tvYear: TextView
    private lateinit var tvGenre: TextView
    private lateinit var tvCountry: TextView
    private lateinit var btnBack: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val track = intent.getParcelableExtra<Track>("TRACK")

        initializeViews()

        if (track != null) {
            try {
                Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                    .fitCenter().placeholder(R.drawable.ic_placeholder_album)
                    .into(findViewById<ImageView>(R.id.ivAlbumPhoto))
            } catch (e: Exception) {
                findViewById<ImageView>(R.id.ivAlbumPhoto).setImageResource(R.drawable.ic_placeholder_album)
            }
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            tvTrackCurrentTime.text = "0:30"
            tvDuration.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            tvGenre.text = track.primaryGenreName
            tvCountry.text = track.country

            if (track.collectionName != null) {
                tvAlbum.text = track.collectionName
                tvAlbum.isVisible = true
                tvAlbumTitle.isVisible = true
            } else {
                tvAlbum.isVisible = false
                tvAlbumTitle.isVisible = false
            }
            if (track.releaseDate != null) {
                tvYear.text = track.releaseDate!!.split('-')[0]
                tvYear.isVisible = true
                tvYearTitle.isVisible = true
            } else {
                tvYear.isVisible = false
                tvYearTitle.isVisible = false
            }
        } else {
            finish()
        }

        findViewById<ImageButton>(R.id.btnBackFromPlayer).setOnClickListener {
            finish()
        }
    }

    private fun initializeViews() {
        ivAlbumPhoto = findViewById(R.id.ivAlbumPhoto)
        tvTrackName = findViewById(R.id.trackName)
        tvArtistName = findViewById(R.id.tvTrackArtistName)
        tvTrackCurrentTime = findViewById(R.id.tvTrackTime)
        tvDuration = findViewById(R.id.tvDuration)
        tvAlbumTitle = findViewById(R.id.TextViewAlbum)
        tvAlbum = findViewById(R.id.tvAlbum)
        tvYearTitle = findViewById(R.id.TextViewYear)
        tvYear = findViewById(R.id.tvYear)
        tvGenre = findViewById(R.id.tvGenre)
        tvCountry = findViewById(R.id.tvCountry)
        btnBack = findViewById(R.id.btnBackFromPlayer)
    }
}