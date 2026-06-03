package com.practicum.playlistmaker.library.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.favorite.ui.FavoriteState
import com.practicum.playlistmaker.favorite.ui.FavoriteViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.PlaylistMakerTheme
import java.text.SimpleDateFormat

@Composable
fun FavoriteTracksScreen(
    viewModel: FavoriteViewModel, onTrackClicked: (Track) -> Unit
) {
    PlaylistMakerTheme {
        val state by viewModel.state.collectAsStateWithLifecycle()

        when (val s = state) {
            is FavoriteState.Default -> ListOfTracks(s.tracks) {
                onTrackClicked(it)
            }

            FavoriteState.Empty -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(106.dp))
                    ErrorPlaceholder(stringResource(R.string.your_library_is_empty))
                }
            }
            FavoriteState.Loading -> {}
        }
    }
}

@Composable
private fun ListOfTracks(list: List<Track>, onTrackClicked: (Track) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = list, key = { it.trackId }) {
            SongItem(
                imageUrl = it.artworkUrl100,
                songName = it.trackName,
                songDuration = SimpleDateFormat("mm:ss", LocalLocale.current.platformLocale).format(
                    it.trackTimeMillis
                ),
                artist = it.artistName
            ) {
                it.isFavorite = true
                onTrackClicked(it)
            }
        }
    }
}

@Composable
fun ErrorPlaceholder(
    message: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_empty_search_output), contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = message,
            fontSize = 19.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SongItem(
    imageUrl: String,
    songName: String,
    songDuration: String,
    artist: String,
    onItemClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 13.dp, vertical = 8.dp)
            .clickable(
                onClick = onItemClicked
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_placeholder_album),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp))
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = songName,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.W400,
                lineHeight = 1.sp
            )
            Row() {
                Text(
                    text = artist,
                    fontSize = 11.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    fontWeight = FontWeight.W400,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    lineHeight = 1.sp
                )
                Image(
                    painter = painterResource(R.drawable.ic_dot), contentDescription = null
                )
                Text(
                    text = songDuration,
                    fontSize = 11.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    fontWeight = FontWeight.W400,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    lineHeight = 1.sp
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.ic_arrow_forward), contentDescription = null
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SongItemPreview() {
    PlaylistMakerTheme() {
        SongItem(
            imageUrl = "asdasa",
            songName = "Hello",
            songDuration = "2:43",
            artist = "Adele",
            onItemClicked = {})
    }
}

@Composable
@Preview(showBackground = true)
private fun ErrorPlaceholderPreview() {
    PlaylistMakerTheme {
        ErrorPlaceholder("Ваша медиатека пуста")
    }
}