package com.practicum.playlistmaker.library.ui.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.ui.ErrorPlaceholder
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.ui.BlackButton
import com.practicum.playlistmaker.utils.PlaylistMakerTheme
import java.io.File


@Composable
fun PlaylistsListScreen(
    viewModel: PlaylistViewModel,
    onItemPlaylistClicked: (Long) -> Unit,
    onButtonCreatePlaylistClicked: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PlaylistMakerTheme {
        when (val s = state) {
            is PlaylistState.Default -> PlaylistsList(onItemPlaylistClicked, s.list, onButtonCreatePlaylistClicked)
            PlaylistState.Empty -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BlackButton(
                        onButtonClicked = onButtonCreatePlaylistClicked,
                        text = stringResource(R.string.new_playlist),
                        modifier = Modifier.padding(top = 24.dp)
                    )
                    Spacer(Modifier.height(46.dp))
                    ErrorPlaceholder(message = stringResource(R.string.you_did_not_create_playlists))
                }
            }
            PlaylistState.Loading -> {}
        }
    }
}

@Composable
private fun PlaylistsList(
    onItemPlaylistClicked: (Long) -> Unit, listOfPlaylist: List<Playlist>, onButtonCreatePlaylistClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .padding(top = 24.dp)
                .background(
                    shape = RoundedCornerShape(54.dp), color = MaterialTheme.colorScheme.primary
                ),
            onClick = {onButtonCreatePlaylistClicked()},
        ) {
            Text(
                text = stringResource(R.string.new_playlist),
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.background,
                lineHeight = 16.sp
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(listOfPlaylist) {
                ItemPlaylist(
                    imageUri = it.uri, name = it.name, tracksCount = it.tracksIds.size
                ) {
                    onItemPlaylistClicked(it.id)
                }
            }
        }
    }
}

@Composable
private fun ItemPlaylist(
    imageUri: String, name: String, tracksCount: Int, onItemPlaylistClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemPlaylistClicked)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        val model = remember(imageUri) {
            if (imageUri.isEmpty()) null
            else File(imageUri).takeIf { it.exists() }
        }
        AsyncImage(
            model = model,
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_placeholder_album),
            fallback = painterResource(R.drawable.ic_placeholder_album),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = name,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
        Text(
            text = LocalResources.current.getQuantityString(
                R.plurals.tracks_count, tracksCount, tracksCount
            ),
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
@Preview
private fun ItemPlaylistPreview() {
    PlaylistMakerTheme() {
        ItemPlaylist(
            imageUri = "", name = "Топ", tracksCount = 25
        ) {

        }
    }
}

//@Composable
//@Preview
//private fun PlaylistsListScreenPreview() {
//    PlaylistMakerTheme {
//        PlaylistsListScreen(
//            list = listOf(
//                Playlist(
//                    id = 1,
//                    name = "my",
//                    description = "sdasd",
//                    uri = "asdas",
//                    tracksIds = emptyList()
//                ),
//                Playlist(
//                    id = 1,
//                    name = "my",
//                    description = "sdasd",
//                    uri = "asdas",
//                    tracksIds = emptyList()
//                )
//            )
//        ) { }
//    }
//}