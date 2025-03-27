package com.lambao.mrbeast.data.remote.service

import com.lambao.mrbeast.data.remote.dto.SongDto
import com.lambao.mrbeast.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface SongApiService {

    @GET("${Constants.URL_VER}?hotsong")
    suspend fun getSongs(): Response<List<SongDto>>
}