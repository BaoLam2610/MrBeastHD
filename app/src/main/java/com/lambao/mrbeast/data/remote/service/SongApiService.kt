package com.lambao.mrbeast.data.remote.service

import com.lambao.mrbeast.data.remote.dto.SongDto
import com.lambao.mrbeast.data.remote.dto.SongInfoDto
import com.lambao.mrbeast.utils.Constants
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface SongApiService {

    @GET("${Constants.URL_VER}?hotsong")
    suspend fun getSongs(): Response<List<SongDto>>

    @FormUrlEncoded
    @POST(Constants.URL_VER)
    suspend fun getSongInfo(
        @Field("link") link: String?
    ): Response<SongInfoDto>
}