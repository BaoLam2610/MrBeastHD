package com.lambao.mrbeast.domain.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: String,
    val title: String = "",
    val artistsNames: String = "",
    val link: String = "",
    val lyric: String = "",
    val thumbnail: String = "",
    val thumbnailUri: Uri? = null,
    val thumbnailBitmap: Bitmap? = null,
    val duration: Long = 0L,
    val data: String = "",
    val filePath: String = "",
    val isOnline: Boolean = false
) : Parcelable