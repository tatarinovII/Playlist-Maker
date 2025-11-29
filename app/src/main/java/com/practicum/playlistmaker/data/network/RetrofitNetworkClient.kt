package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    val retrofitApi: Retrofit = Retrofit.Builder().baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val songApi = retrofitApi.create(SongApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val response = songApi.search(dto.expression).execute()
            val body = response.body() ?: Response()
            return body.apply { resultCode = response.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}