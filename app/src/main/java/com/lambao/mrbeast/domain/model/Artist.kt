package com.lambao.mrbeast.domain.model

import com.lambao.mrbeast.data.remote.dto.ArtistDto

data class Artist(
    val id: String,
    val name: String,
    val link: String,
    val cover: String,
    val thumbnail: String,
)

fun ArtistDto.toArtist() = Artist(
    id = id ?: "",
    name = name ?: "",
    link = link ?: "",
    cover = cover ?: "",
    thumbnail = thumbnail ?: "",
)