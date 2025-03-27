package com.lambao.mrbeast.data.remote.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArtistDto(
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("link") val link: String?,
    @Expose @SerializedName("cover") val cover: String?,
    @Expose @SerializedName("thumbnail") val thumbnail: String?
)
