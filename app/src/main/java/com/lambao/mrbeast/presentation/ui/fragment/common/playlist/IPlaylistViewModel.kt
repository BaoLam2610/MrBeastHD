package com.lambao.mrbeast.presentation.ui.fragment.common.playlist

import com.lambao.mrbeast.domain.model.Song
import kotlinx.coroutines.flow.StateFlow

interface IPlaylistViewModel {
    fun getPlaylist(): StateFlow<List<Song>>
    fun getPlaylistValue(): List<Song>
    fun setPlaylistAsync(playlist: List<Song>)
    fun setPlaylist(playlist: List<Song>)
}