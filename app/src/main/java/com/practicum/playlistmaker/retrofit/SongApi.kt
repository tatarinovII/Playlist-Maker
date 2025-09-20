package com.practicum.playlistmaker.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<SongSearchResponse>
}
