package com.lambao.mrbeast.domain.usecase

import com.lambao.base.data.Resource
import com.lambao.base.data.map
import com.lambao.base.domain.FlowUseCase
import com.lambao.mrbeast.data.repository.OnlineSongsRepository
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.model.OnlineSongInfo
import com.lambao.mrbeast.domain.model.toOnlineSongInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOnlineSongInfoUseCase @Inject constructor(
    private val repository: OnlineSongsRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<String, Resource<OnlineSongInfo>>(ioDispatcher) {
    override fun execute(params: String?): Flow<Resource<OnlineSongInfo>> {
        return repository.getSongInfo(params).map { resource ->
            resource.map { it.toOnlineSongInfo() }
        }
    }
}