package com.lambao.mrbeast.domain.usecase

import com.lambao.base.data.Resource
import com.lambao.base.data.mapList
import com.lambao.base.domain.FlowUseCase
import com.lambao.mrbeast.data.repository.online.OnlinePlaylistRepository
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.model.toSong
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOnlinePlaylistUseCase @Inject constructor(
    private val repository: OnlinePlaylistRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Resource<List<Song>>>(ioDispatcher) {
    override fun execute(params: Unit?): Flow<Resource<List<Song>>> {
        return repository.getPlaylist().map { resource ->
            resource.mapList { it.toSong() }
        }
    }
}