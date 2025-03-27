package com.lambao.mrbeast.data.repository

import com.lambao.base.data.Resource
import com.lambao.mrbeast.data.remote.dto.SongDto
import kotlinx.coroutines.flow.Flow

interface OnlineSongsRepository {
    fun getSongs(): Flow<Resource<List<SongDto>>>
}