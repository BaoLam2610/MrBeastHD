package com.lambao.mrbeast.data.repository

import com.lambao.base.data.Resource
import com.lambao.mrbeast.data.remote.dto.SongDto
import com.lambao.mrbeast.data.remote.dto.SongInfoDto
import kotlinx.coroutines.flow.Flow

interface OnlineSongsRepository {
    fun getSongs(): Flow<Resource<List<SongDto>>>
    fun getSongInfo(link: String?): Flow<Resource<SongInfoDto>>
}