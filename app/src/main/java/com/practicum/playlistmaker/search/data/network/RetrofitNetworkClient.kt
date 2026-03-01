package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest

class RetrofitNetworkClient(
    private val songApi: SongApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode == -1 }
        }
        return try {
                val response = songApi.search(dto.expression)
                response.apply { resultCode = 200 }

            } catch (_: Throwable) {
                Response().apply { resultCode = -1 }
            }
        }
    }