package com.lambao.mrbeast.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Thumbnail(
    val url: String
) : Parcelable
