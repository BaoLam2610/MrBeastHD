package com.lambao.mrbeast.domain.model

import android.os.Parcelable
import com.lambao.mrbeast.data.remote.dto.SongDto
import com.lambao.mrbeast.extension.toTimeString
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: String,
    val name: String,
    val title: String,
    val code: String,
    val contentOwner: Long,
    val isOfficial: Boolean,
    val isWorldWide: Boolean,
    val playlistID: String,
    val artists: List<Artist>,
    val artistsNames: String,
    val performer: String,
    val type: String,
    val link: String,
    val lyric: String,
    val thumbnail: String,
    val duration: Long,
    val total: Long,
    val rankNum: String,
    val rankStatus: String,
    val artist: Artist?,
    val position: Long,
    val order: String,
    val album: Album?,
    val data: String = "https://vnso-zn-24-tf-a128-z3.zmdcdn.me/31cb17656c5146f10de0247036f2772d?authen=exp=1743688643~acl=/31cb17656c5146f10de0247036f2772d*~hmac=9dce13b6080fe8d96308d1e30866a73d&fs=MHx3ZWJWNXwxMDMdUngNTmUsICdUngMjIxLjI3"
) : Parcelable {
    fun getDurationTime() = duration.toTimeString()
}

fun SongDto.toSong() = Song(
    id = id ?: "",
    name = name ?: "",
    title = title ?: "",
    code = code ?: "",
    contentOwner = contentOwner ?: 0L,
    isOfficial = isOfficial == true,
    isWorldWide = isWorldWide == true,
    playlistID = playlistID ?: "",
    artists = artists?.map { it.toArtist() } ?: emptyList(),
    artistsNames = artistsNames ?: "",
    performer = performer ?: "",
    type = type ?: "",
    link = link ?: "",
    lyric = lyric ?: "",
    thumbnail = thumbnail ?: "",
    duration = duration ?: 0L,
    total = total ?: 0L,
    rankNum = rankNum ?: "",
    rankStatus = rankStatus ?: "",
    artist = artist?.toArtist(),
    position = position ?: 0L,
    order = order ?: "",
    album = album?.toAlbum(),
)