package com.practicum.playlistmaker.di


import com.practicum.playlistmaker.favorite.ui.FavoriteViewModel
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.playlist.ui.editplaylist.EditPlayListViewModel
import com.practicum.playlistmaker.playlist.ui.newplaylist.NewPlaylistViewModel
import com.practicum.playlistmaker.playlist.ui.playlist.PlaylistViewModel
import com.practicum.playlistmaker.playlist.ui.playlist_page.PlaylistPageViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<PlayerViewModel> { (track: Track) ->
        PlayerViewModel(track, get(), get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }

    viewModel<FavoriteViewModel> {
        FavoriteViewModel(get())
    }

    viewModel<PlaylistViewModel> {
        PlaylistViewModel(get())
    }

    viewModel<NewPlaylistViewModel>() {
        NewPlaylistViewModel(get(), get())
    }

    viewModel<PlaylistPageViewModel>() { (trackId: Long) ->
        PlaylistPageViewModel(trackId, get(), get(), get())
    }

    viewModel<EditPlayListViewModel>() {
        EditPlayListViewModel(get(), get())
    }
}