package com.lambao.mrbeast.presentation.ui.fragment.play_song

import androidx.lifecycle.viewModelScope
import com.lambao.base.presentation.ui.viewmodel.network.NetworkViewModel
import com.lambao.mrbeast.di.DefaultDispatcher
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.model.Thumbnail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlaySongViewModel @Inject constructor(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : NetworkViewModel(ioDispatcher, defaultDispatcher) {

    private val _song = MutableStateFlow<Song?>(null)
    val song get() = _song.asStateFlow()

    private val _songList = MutableStateFlow<List<Song>>(emptyList())
    val songList get() = _songList.asStateFlow()

    private val _songThumbnails = _songList.map {
        it.map { Thumbnail(it.thumbnail) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val songThumbnails get() = _songThumbnails

    fun setSong(song: Song) {
        launch {
            _song.emit(song)
        }
    }

    fun setSongList(songList: List<Song>) {
        launch {
            _songList.emit(songList)
        }
    }
}