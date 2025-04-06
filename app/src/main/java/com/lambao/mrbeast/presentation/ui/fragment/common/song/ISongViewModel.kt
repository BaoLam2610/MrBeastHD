package com.lambao.mrbeast.presentation.ui.fragment.common.song

import com.lambao.mrbeast.domain.model.Song
import kotlinx.coroutines.flow.StateFlow

interface ISongViewModel {
    val song: StateFlow<Song?>
    val songValue: Song?
    fun setSongAsync(song: Song?)
    fun setSong(song: Song?)
}