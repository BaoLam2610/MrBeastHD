package com.lambao.mrbeast.data.remote.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SongInfoRemoteDto(
    @Expose @SerializedName("success") val success: String? = null
)