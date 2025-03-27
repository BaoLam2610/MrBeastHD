package com.lambao.mrbeast.data.remote.service

import com.lambao.mrbeast.data.remote.dto.SongDto
import retrofit2.Response
import retrofit2.http.GET

interface SongApiService {

    @GET("hotsong")
    suspend fun getSongs(): Response<List<SongDto>>
}