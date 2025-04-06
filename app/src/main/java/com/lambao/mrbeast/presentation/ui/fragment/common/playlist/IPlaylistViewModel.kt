package com.lambao.mrbeast.presentation.ui.fragment.common.playlist

import com.lambao.mrbeast.domain.model.Song
import kotlinx.coroutines.flow.StateFlow

interface IPlaylistViewModel {
    val playlist: StateFlow<List<Song>>
    val playlistValue: List<Song>
    fun setPlaylistAsync(playlist: List<Song>)
    fun setPlaylist(playlist: List<Song>)
}