package com.lambao.mrbeast.data.repository.online

import com.lambao.base.data.Resource
import com.lambao.mrbeast.domain.model.OnlineSongInfo
import com.lambao.mrbeast.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface OnlinePlaylistRepository {
    fun getPlaylist(): Flow<Resource<List<Song>>>
    fun getSongInfo(link: String?): Flow<Resource<OnlineSongInfo>>
}