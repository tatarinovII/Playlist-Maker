package com.practicum.playlistmaker.library.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.favorite.ui.FavoriteViewModel
import com.practicum.playlistmaker.library.ui.playlist.PlaylistViewModel
import com.practicum.playlistmaker.library.ui.playlist.PlaylistsListScreen
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.PlaylistMakerTheme
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen(
    playlistViewModel: PlaylistViewModel,
    favoriteViewModel: FavoriteViewModel,
    onTrackClicked: (Track) -> Unit,
    onItemPlaylistClicked: (Long) -> Unit,
    onButtonCreatePlaylistClicked: () -> Unit

) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex = pagerState.currentPage
    PlaylistMakerTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                text = stringResource(R.string.library),
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                fontWeight = FontWeight.W400,
                textAlign = TextAlign.Start
            )
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                divider = {},
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.favorite_tracks),
                            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                            fontWeight = FontWeight.W500,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.playlists),
                            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                            fontWeight = FontWeight.W500,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                )
            }

            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> FavoriteTracksScreen(
                        viewModel = favoriteViewModel, onTrackClicked = onTrackClicked
                    )

                    1 -> PlaylistsListScreen(
                        viewModel = playlistViewModel,
                        onItemPlaylistClicked = onItemPlaylistClicked,
                        onButtonCreatePlaylistClicked = onButtonCreatePlaylistClicked
                    )
                }
            }
        }
    }
}