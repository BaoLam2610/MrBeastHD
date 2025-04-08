package com.lambao.mrbeast.data.local.model

import android.net.Uri

data class SongLocalDto(
    val id: String?,
    val title: String?,
    val artist: String?,
    val album: String?,
    val duration: Long?,
    val filePath: String?,
    val fileSize: Long?,
    val thumbnail: Uri?
)