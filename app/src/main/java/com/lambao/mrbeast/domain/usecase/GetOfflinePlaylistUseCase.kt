package com.lambao.mrbeast.domain.usecase

import com.lambao.base.data.Resource
import com.lambao.base.data.mapList
import com.lambao.base.domain.FlowUseCase
import com.lambao.mrbeast.data.repository.offline.OfflinePlaylistRepository
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.mapping.toSong
import com.lambao.mrbeast.domain.model.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOfflinePlaylistUseCase @Inject constructor(
    private val repository: OfflinePlaylistRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Resource<List<Song>>>(ioDispatcher) {
    override fun execute(params: Unit?): Flow<Resource<List<Song>>> {
        return repository.getPlaylist().map { it.mapList { it.toSong() } }
    }
}