package com.lambao.mrbeast.domain.mapping

import com.lambao.mrbeast.data.local.model.SongLocalDto
import com.lambao.mrbeast.data.remote.dto.SongRemoteDto
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.utils.Constants.ZINGMP3_DOMAIN

fun SongRemoteDto.toSong() = Song(
    id = id ?: "",
    title = title ?: "",
    artistsNames = artistsNames ?: "",
    link = if (link.isNullOrEmpty()) "" else ZINGMP3_DOMAIN + link,
    lyric = lyric ?: "",
    thumbnail = thumbnail ?: "",
    duration = duration ?: 0L,
    isOnline = true
)

fun SongLocalDto.toSong() =
    Song(
        id = id ?: "",
        title = title ?: "",
        artistsNames = artist ?: "",
        duration = duration ?: 0L,
        data = filePath ?: "",
        thumbnailBitmap = thumbnail,
        isOnline = false
    )