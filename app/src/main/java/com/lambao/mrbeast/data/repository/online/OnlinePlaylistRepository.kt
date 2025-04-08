package com.lambao.mrbeast.data.repository.online

import com.lambao.base.data.Resource
import com.lambao.mrbeast.data.remote.dto.SongRemoteDto
import com.lambao.mrbeast.data.remote.dto.SongInfoRemoteDto
import kotlinx.coroutines.flow.Flow

interface OnlinePlaylistRepository {
    fun getPlaylist(): Flow<Resource<List<SongRemoteDto>>>
    fun getSongInfo(link: String?): Flow<Resource<SongInfoRemoteDto>>
}