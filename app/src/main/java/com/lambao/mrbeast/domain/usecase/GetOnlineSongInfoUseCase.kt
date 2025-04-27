package com.lambao.mrbeast.domain.usecase

import com.lambao.base.data.Resource
import com.lambao.base.data.map
import com.lambao.base.domain.FlowUseCase
import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import com.lambao.mrbeast.data.repository.online.OnlinePlaylistRepository
import com.lambao.mrbeast.domain.model.OnlineSongInfo
import com.lambao.mrbeast.domain.model.toOnlineSongInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetOnlineSongInfoUseCase @Inject constructor(
    private val repository: OnlinePlaylistRepository,
    dispatcherProvider: DispatcherProvider
) : FlowUseCase<String, Resource<OnlineSongInfo>>(dispatcherProvider) {
    override fun execute(params: String?): Flow<Resource<OnlineSongInfo>> {
        return repository.getSongInfo(params).map { resource ->
            resource.map { it.toOnlineSongInfo() }
        }
    }
}