package com.lambao.mrbeast.domain.model

import com.lambao.mrbeast.data.remote.dto.AlbumDto

data class Album(
    val id: String,
    val link: String,
    val title: String,
    val name: String,
    val isOfficial: Boolean,
    val artistsNames: String,
    val artists: List<Artist>,
    val thumbnail: String,
    val thumbnailMedium: String
)

fun AlbumDto.toAlbum() = Album(
    id = id ?: "",
    link = link ?: "",
    title = title ?: "",
    name = name ?: "",
    isOfficial = isOfficial == true,
    artistsNames = artistsNames ?: "",
    artists = artists?.map { it.toArtist() } ?: emptyList(),
    thumbnail = thumbnail ?: "",
    thumbnailMedium = thumbnailMedium ?: ""
)
