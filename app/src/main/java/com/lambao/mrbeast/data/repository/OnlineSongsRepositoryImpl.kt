package com.lambao.mrbeast.data.repository

import com.google.gson.Gson
import com.lambao.base.data.remote.BaseRemoteDataSource
import com.lambao.mrbeast.data.remote.service.SongApiService
import com.lambao.mrbeast.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class OnlineSongsRepositoryImpl @Inject constructor(
    private val apiService: SongApiService,
    gson: Gson,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : OnlineSongsRepository, BaseRemoteDataSource(gson, ioDispatcher) {
    override fun getSongs() = safeCall {
        apiService.getSongs()
    }
}