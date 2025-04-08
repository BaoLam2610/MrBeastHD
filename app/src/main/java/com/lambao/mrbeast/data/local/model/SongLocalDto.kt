package com.lambao.mrbeast.data.local.model

import android.graphics.Bitmap

data class SongLocalDto(
    val id: String?,
    val title: String?,
    val artist: String?,
    val album: String?,
    val duration: Long?,
    val filePath: String?,
    val fileSize: Long?,
    val thumbnail: Bitmap?
)