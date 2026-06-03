package com.practicum.playlistmaker.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.ui.ErrorPlaceholder
import com.practicum.playlistmaker.library.ui.SongItem
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.models.SearchState
import com.practicum.playlistmaker.utils.PlaylistMakerTheme
import java.text.SimpleDateFormat


@Composable
fun SearchScreen(
    viewModel: SearchViewModel, onTrackItemClicked: (Track) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var searchText by rememberSaveable { mutableStateOf("") }
    var isFocused by rememberSaveable { mutableStateOf(false) }
    PlaylistMakerTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SearchBar(value = searchText, onTextChanged = {
                searchText = it
                if (isFocused && it.isEmpty()) {
                    viewModel.showSearchHistory()
                } else viewModel.searchDebounce(searchText)
            }, onFocusChanged = {
                isFocused = it
                if (it && searchText.isEmpty()) {
                    viewModel.showSearchHistory()
                }
            }, onButtonClearTextFieldClicked = {
                searchText = ""
                viewModel.clearTracks()
            })

            when (val s = state) {
                is SearchState.ShowSearchHistory -> {
                    HistoryComponent(
                        list = s.historyList,
                        onTrackItemClicked = {
                            onTrackItemClicked(it)
                            viewModel.addTrackToHistory(it)
                        },
                        onButtonClearHistoryClicked = {
                            viewModel.onButtonClearHistoryClicked()
                        })
                }

                SearchState.Default -> {}
                SearchState.Loading -> {
                    SearchProgressBar()
                }

                SearchState.EmptyOutput -> {
                    ErrorPlaceholder(stringResource(R.string.no_search_output))
                }

                SearchState.InternetConnectionError -> {
                    InternetConnectionError {
                        viewModel.searchDebounce(searchText)
                    }
                }

                is SearchState.ShowOutput -> {
                    SearchOutput(
                        onTrackItemClicked = {
                            onTrackItemClicked(it)
                            viewModel.addTrackToHistory(it)
                        },
                        list = s.searchOutput
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchOutput(
    onTrackItemClicked: (Track) -> Unit, list: List<Track>
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(
            items = list, key = { it.trackId }) {
            SongItem(
                imageUrl = it.artworkUrl100,
                songName = it.trackName,
                songDuration = SimpleDateFormat("mm:ss", LocalLocale.current.platformLocale).format(
                    it.trackTimeMillis
                ),
                artist = it.artistName,
                onItemClicked = {
                    onTrackItemClicked(it)
                })
        }
    }
}

@Composable
private fun SearchBar(
    value: String,
    onTextChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onButtonClearTextFieldClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            text = stringResource(R.string.search),
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Start
        )
        SearchTextField(
            onTextChanged = onTextChanged,
            onButtonClearClicked = onButtonClearTextFieldClicked,
            value = value,
            onFocusChanged = onFocusChanged
        )
    }
}

@Composable
private fun SearchTextField(
    onTextChanged: (String) -> Unit,
    onButtonClearClicked: () -> Unit,
    value: String,
    onFocusChanged: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(36.dp)
            .background(
                shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surface
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.padding(start = 12.dp),
            painter = painterResource(R.drawable.ic_search_2),
            contentDescription = null
        )
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .padding(start = 8.dp)
                .onFocusChanged { onFocusChanged(it.isFocused) },
            value = value,
            onValueChange = onTextChanged,
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                color = Color.Black
            ),
            decorationBox = { a ->
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(R.string.search),
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                a()
            })
        if (value.isNotEmpty()) {
            Image(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable(onClick = {
                        onButtonClearClicked()
                    }),
                painter = painterResource(R.drawable.ic_clear_search),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun HistoryComponent(
    list: List<Track>, onTrackItemClicked: (Track) -> Unit, onButtonClearHistoryClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.you_searched),
            fontWeight = FontWeight.W500,
            fontSize = 19.sp,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            textAlign = TextAlign.Center
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            items(
                items = list, key = { it.trackId }) {
                SongItem(
                    imageUrl = it.artworkUrl100,
                    songName = it.trackName,
                    songDuration = SimpleDateFormat(
                        "mm:ss", LocalLocale.current.platformLocale
                    ).format(it.trackTimeMillis),
                    artist = it.artistName,
                    onItemClicked = {
                        onTrackItemClicked(it)
                    })
            }
        }
        BlackButton(
            onButtonClicked = onButtonClearHistoryClicked,
            text = stringResource(R.string.clear_search_history),
            modifier = Modifier.padding(top = 24.dp)
        )

    }
}

@Composable
private fun InternetConnectionError(
    onButtonRefreshClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(102.dp))
        Image(
            painter = painterResource(R.drawable.ic_connection_error), contentDescription = null
        )
        Text(
            text = stringResource(R.string.search_connection_error),
            textAlign = TextAlign.Center,
            fontSize = 19.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily(Font(R.font.ys_display_medium))
        )
        BlackButton(
            onButtonClicked = { onButtonRefreshClicked() },
            text = stringResource(R.string.refresh),
            modifier = Modifier.padding(top = 24.dp),
        )
    }
}

@Composable
fun BlackButton(
    onButtonClicked: () -> Unit, text: String, modifier: Modifier = Modifier
) {
    Button(
        modifier = Modifier.padding(top = 24.dp),
        onClick = onButtonClicked,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.background,
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontWeight = FontWeight.W500,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun SearchProgressBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 140.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(dimensionResource(R.dimen.progress_bar_size)),
            color = colorResource(R.color.blue)
        )
    }
}