package com.lambao.mrbeast.presentation.ui.fragment.play_song

import androidx.lifecycle.viewModelScope
import com.lambao.base.presentation.ui.viewmodel.network.NetworkViewModel
import com.lambao.mrbeast.di.DefaultDispatcher
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.model.PlaybackEvent
import com.lambao.mrbeast.domain.usecase.GetIndexInPlaylistUseCase
import com.lambao.mrbeast.presentation.ui.fragment.common.playback.IPlaybackViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.playback.PlaybackViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.playlist.IPlaylistViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.playlist.PlaylistViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.song.ISongViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.song.SongViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlaySongViewModel @Inject constructor(
    private val getIndexInPlaylistUseCase: GetIndexInPlaylistUseCase,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : NetworkViewModel(ioDispatcher, defaultDispatcher),
    ISongViewModel by SongViewModel(),
    IPlaylistViewModel by PlaylistViewModel(),
    IPlaybackViewModel by PlaybackViewModel() {

    private val _currentSongIndex = MutableStateFlow(-1)
    val currentSongIndex get() = _currentSongIndex.asStateFlow()
    val currentSongIndexValue get() = _currentSongIndex.value

    private val _combineIndexInPlaylist = combine(
        song,
        playlist
    ) { song, playlist ->
        getIndexInPlaylistUseCase.invoke(song, playlist)
    }.stateIn(viewModelScope, SharingStarted.Lazily, -1)
    val combineIndexInPlaylist get() = _combineIndexInPlaylist

    private val _shouldPlaySong = combine(
        _currentSongIndex,
        playlist
    ) { index, playlist ->
        index != -1 && index < playlist.size && playlist.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val shouldPlaySong get() = _shouldPlaySong

    fun setCurrentSongIndex(index: Int) {
        launch {
            _currentSongIndex.emit(index)
        }
    }

    fun previousSong() {
        launch {
            if (currentSongIndex.value > 0) {
                _currentSongIndex.emit(currentSongIndex.value - 1)
                setPlaybackEvent(PlaybackEvent.PREVIOUS)
            }
        }
    }

    fun nextSong() {
        launch {
            if (currentSongIndex.value < playlistValue.size - 1) {
                _currentSongIndex.emit(currentSongIndex.value + 1)
                setPlaybackEvent(PlaybackEvent.NEXT)
            }
        }
    }
}