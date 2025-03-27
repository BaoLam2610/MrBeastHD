package com.lambao.mrbeast.data.remote.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SongDto(
    @Expose @SerializedName("id") val id: String,
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("title") val title: String,
    @Expose @SerializedName("code") val code: String,
    @Expose @SerializedName("content_owner") val contentOwner: Long,
    @Expose @SerializedName("isoffical") val isOfficial: Boolean,
    @Expose @SerializedName("isWorldWide") val isWorldWide: Boolean,
    @Expose @SerializedName("playlist_id") val playlistID: String,
    @Expose @SerializedName("artists") val artists: List<ArtistDto>,
    @Expose @SerializedName("artists_names") val artistsNames: String,
    @Expose @SerializedName("performer") val performer: String,
    @Expose @SerializedName("type") val type: String,
    @Expose @SerializedName("link") val link: String,
    @Expose @SerializedName("lyric") val lyric: String,
    @Expose @SerializedName("thumbnail") val thumbnail: String,
    @Expose @SerializedName("duration") val duration: Long,
    @Expose @SerializedName("total") val total: Long,
    @Expose @SerializedName("rank_num") val rankNum: String,
    @Expose @SerializedName("rank_status") val rankStatus: String,
    @Expose @SerializedName("artist") val artist: ArtistDto,
    @Expose @SerializedName("position") val position: Long,
    @Expose @SerializedName("order") val order: String,
    @Expose @SerializedName("album") val album: AlbumDto,
)
