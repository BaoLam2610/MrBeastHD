package com.lambao.mrbeast.presentation.ui.fragment.common.playlist

import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.mrbeast.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow

class PlaylistViewModel : BaseViewModel(), IPlaylistViewModel {
    private val _playlist = MutableStateFlow<List<Song>>(emptyList())

    override fun getPlaylist() = _playlist

    override fun getPlaylistValue() = _playlist.value

    override fun setPlaylistAsync(playlist: List<Song>) {
        launch { _playlist.emit(playlist) }
    }

    override fun setPlaylist(playlist: List<Song>) {
        _playlist.value = playlist
    }
}