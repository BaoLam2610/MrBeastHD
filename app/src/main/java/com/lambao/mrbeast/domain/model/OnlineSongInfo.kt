package com.lambao.mrbeast.domain.model

import com.lambao.mrbeast.data.remote.dto.SongInfoRemoteDto

data class OnlineSongInfo(
    val mp3Url: String,
    val thumbnail: String // high quality thumbnail URL
)

fun SongInfoRemoteDto.toOnlineSongInfo(): OnlineSongInfo =
    if (success == null) {
        OnlineSongInfo("", "")
    } else {
        try {
            val cleanedHtml = success.replace("\\s+".toRegex(), " ")
            val imageRegex = """data-image="([^"]+)"""".toRegex()
            val srcRegex = """data-src="([^"]+)"""".toRegex()

            val imageUrl = imageRegex.find(cleanedHtml)?.groupValues?.getOrNull(1) ?: ""
            val mp3Url = srcRegex.find(cleanedHtml)?.groupValues?.getOrNull(1) ?: ""
            OnlineSongInfo(mp3Url, imageUrl)
        } catch (e: Exception) {
            OnlineSongInfo("", "")
        }
    }

