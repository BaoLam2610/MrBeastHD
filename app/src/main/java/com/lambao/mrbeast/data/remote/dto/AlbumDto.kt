package com.lambao.mrbeast.data.remote.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AlbumDto(
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("link") val link: String?,
    @Expose @SerializedName("title") val title: String?,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("isoffical") val isOfficial: Boolean?,
    @Expose @SerializedName("artists_names") val artistsNames: String?,
    @Expose @SerializedName("artists") val artists: List<ArtistDto>?,
    @Expose @SerializedName("thumbnail") val thumbnail: String?,
    @Expose @SerializedName("thumbnail_medium") val thumbnailMedium: String?
)
