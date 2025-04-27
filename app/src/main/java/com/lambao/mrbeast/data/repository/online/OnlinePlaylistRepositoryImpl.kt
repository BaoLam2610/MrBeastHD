package com.lambao.mrbeast.data.repository.online

import com.google.gson.Gson
import com.lambao.base.data.map
import com.lambao.base.data.mapList
import com.lambao.base.data.remote.BaseRemoteDataSource
import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import com.lambao.mrbeast.data.remote.service.SongApiService
import com.lambao.mrbeast.domain.mapping.toSong
import com.lambao.mrbeast.domain.model.toOnlineSongInfo
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OnlinePlaylistRepositoryImpl @Inject constructor(
    private val apiService: SongApiService,
    gson: Gson,
    dispatcherProvider: DispatcherProvider
) : OnlinePlaylistRepository, BaseRemoteDataSource(gson, dispatcherProvider) {
    override fun getPlaylist() = safeCall {
        apiService.getSongs()
    }.map { resource ->
        resource.mapList { it.toSong() }
    }

    override fun getSongInfo(link: String?) = safeCall {
        apiService.getSongInfo(link)
    }.map { resource ->
        resource.map { it.toOnlineSongInfo() }
    }
}