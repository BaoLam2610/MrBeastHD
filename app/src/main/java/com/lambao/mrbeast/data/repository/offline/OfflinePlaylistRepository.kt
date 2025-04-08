package com.lambao.mrbeast.data.repository.offline

import com.lambao.base.data.Resource
import com.lambao.mrbeast.data.local.model.SongLocalDto
import kotlinx.coroutines.flow.Flow

interface OfflinePlaylistRepository {
    fun getPlaylist(): Flow<Resource<List<SongLocalDto>>>
}