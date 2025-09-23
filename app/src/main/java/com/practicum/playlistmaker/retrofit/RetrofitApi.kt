package com.practicum.playlistmaker.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApi {

    val retrofitApi: Retrofit = Retrofit.Builder().baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create()).build()
}