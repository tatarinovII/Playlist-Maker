package com.practicum.playlistmaker.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.StorageClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.SongApi
import com.practicum.playlistmaker.search.data.sharedprefs.PrefsStorageClient
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val HISTORY_KEY = "history_key"

val dataModule = module {

    single<SongApi> {
        Retrofit.Builder().baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create()).build().create(SongApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single(named("historyPrefs")) {
        androidContext()
            .getSharedPreferences("HISTORY", Context.MODE_PRIVATE)
    }

    single {
        Gson()
    }

    single<StorageClient<ArrayList<Track>>> {
        PrefsStorageClient(
            prefs = get(named("historyPrefs")),
            gson = get(),
            dataKey = HISTORY_KEY,
            type = object : TypeToken<ArrayList<Track>>() {}.type
        )
    }
    single {
        ExternalNavigator(androidContext())
    }
}