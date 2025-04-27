package com.lambao.mrbeast.domain.usecase

import com.lambao.base.data.Resource
import com.lambao.base.domain.FlowUseCase
import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import com.lambao.mrbeast.data.repository.offline.OfflinePlaylistRepository
import com.lambao.mrbeast.domain.model.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOfflinePlaylistUseCase @Inject constructor(
    private val repository: OfflinePlaylistRepository,
    dispatcherProvider: DispatcherProvider
) : FlowUseCase<Unit, Resource<List<Song>>>(dispatcherProvider) {
    override fun execute(params: Unit?): Flow<Resource<List<Song>>> {
        return repository.getPlaylist()
    }
}