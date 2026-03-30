package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import java.text.SimpleDateFormat
import java.util.Locale

class ExternalNavigator(private val context: Context) {
    fun shareLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun openEmail(emailData: EmailData) {
        val message = emailData.message
        val subject = emailData.subject
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ilya.tatarinov77@gmail.com"))
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun sharePlaylist(playlist: Playlist, list: List<Track>) {
        val shareText = StringBuilder()
        shareText.appendLine(playlist.name)
        shareText.appendLine(playlist.description)
        shareText.appendLine(
            context.resources.getQuantityString(
                R.plurals.tracks_count, playlist.tracksIds.size, playlist.tracksIds.size
            )
        )
        list.forEachIndexed { index, track ->
            shareText.appendLine(
                context.getString(
                    R.string.track_info,
                    index+1,
                    track.artistName,
                    track.trackName,
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
                )
            )
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText.toString())
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}