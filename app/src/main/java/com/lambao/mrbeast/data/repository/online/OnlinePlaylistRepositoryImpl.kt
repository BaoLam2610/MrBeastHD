package com.lambao.mrbeast.data.repository.online

import com.google.gson.Gson
import com.lambao.base.data.remote.BaseRemoteDataSource
import com.lambao.mrbeast.data.remote.service.SongApiService
import com.lambao.mrbeast.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class OnlinePlaylistRepositoryImpl @Inject constructor(
    private val apiService: SongApiService,
    gson: Gson,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : OnlinePlaylistRepository, BaseRemoteDataSource(gson, ioDispatcher) {
    override fun getPlaylist() = safeCall {
        apiService.getSongs()
    }

    override fun getSongInfo(link: String?) = safeCall {
        apiService.getSongInfo(link)
    }
}