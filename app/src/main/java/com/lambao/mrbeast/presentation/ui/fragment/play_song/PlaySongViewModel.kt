package com.lambao.mrbeast.presentation.ui.fragment.play_song

import androidx.lifecycle.viewModelScope
import com.lambao.base.presentation.ui.viewmodel.network.NetworkViewModel
import com.lambao.mrbeast.di.DefaultDispatcher
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.model.Thumbnail
import com.lambao.mrbeast.domain.usecase.GetIndexInPlaylistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlaySongViewModel @Inject constructor(
    private val getIndexInPlaylistUseCase: GetIndexInPlaylistUseCase,
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

    private val _combineIndexInPlaylist = combine(
        _song,
        _songList
    ) { song, playlist ->
        getIndexInPlaylistUseCase.invoke(song, playlist)
    }.stateIn(viewModelScope, SharingStarted.Lazily, -1)
    val combineIndexInPlaylist get() = _combineIndexInPlaylist

    private val _currentSongIndex = MutableStateFlow(-1)
    val currentSongIndex get() = _currentSongIndex

    private val _shouldRegisterPageChangeListener = _currentSongIndex.map {
        it != -1
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val shouldRegisterPageChangeListener get() = _shouldRegisterPageChangeListener

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

    fun setCurrentSongIndex(index: Int) {
        launch {
            _currentSongIndex.emit(index)
        }
    }

    fun previousSong() {
        launch {
            if (currentSongIndex.value > 0) {
                _currentSongIndex.emit(currentSongIndex.value - 1)
            }
        }
    }

    fun nextSong() {
        launch {
            if (currentSongIndex.value < songList.value.size - 1) {
                _currentSongIndex.emit(currentSongIndex.value + 1)
            }
        }
    }
}