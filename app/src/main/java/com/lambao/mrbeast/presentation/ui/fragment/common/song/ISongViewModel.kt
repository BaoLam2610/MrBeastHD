package com.lambao.mrbeast.presentation.ui.fragment.common.song

import com.lambao.mrbeast.domain.model.Song
import kotlinx.coroutines.flow.StateFlow

interface ISongViewModel {
    fun getSong(): StateFlow<Song?>
    fun getSongValue(): Song?
    fun setSongAsync(song: Song?)
    fun setSong(song: Song?)
}