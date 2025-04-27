package com.lambao.mrbeast.domain.usecase

import com.lambao.base.data.Resource
import com.lambao.base.data.mapList
import com.lambao.base.domain.FlowUseCase
import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import com.lambao.mrbeast.data.repository.online.OnlinePlaylistRepository
import com.lambao.mrbeast.domain.mapping.toSong
import com.lambao.mrbeast.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOnlinePlaylistUseCase @Inject constructor(
    private val repository: OnlinePlaylistRepository,
    dispatcherProvider: DispatcherProvider
) : FlowUseCase<Unit, Resource<List<Song>>>(dispatcherProvider) {
    override fun execute(params: Unit?): Flow<Resource<List<Song>>> {
        return repository.getPlaylist().map { resource ->
            resource.mapList { it.toSong() }
        }
    }
}