package com.lambao.mrbeast.presentation.ui.fragment.common.song

import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.mrbeast.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow

class SongViewModel(
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider), ISongViewModel {
    private val _song = MutableStateFlow<Song?>(null)

    override fun getSong() = _song

    override fun getSongValue() = _song.value

    override fun setSongAsync(song: Song?) {
        launch { _song.emit(song) }
    }

    override fun setSong(song: Song?) {
        _song.value = song
    }
}