package com.lambao.mrbeast.domain.model

import android.os.Parcelable
import com.lambao.mrbeast.data.remote.dto.ArtistDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    val id: String,
    val name: String,
    val link: String,
    val cover: String,
    val thumbnail: String,
) : Parcelable

fun ArtistDto.toArtist() = Artist(
    id = id ?: "",
    name = name ?: "",
    link = link ?: "",
    cover = cover ?: "",
    thumbnail = thumbnail ?: "",
)