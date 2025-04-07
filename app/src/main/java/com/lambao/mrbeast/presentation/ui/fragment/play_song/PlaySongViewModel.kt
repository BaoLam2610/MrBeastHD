package com.lambao.mrbeast.presentation.ui.fragment.play_song

import androidx.lifecycle.viewModelScope
import com.lambao.base.presentation.ui.viewmodel.network.NetworkViewModel
import com.lambao.mrbeast.di.DefaultDispatcher
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.model.playback.PlaybackEvent
import com.lambao.mrbeast.domain.service.MediaPlayerManager
import com.lambao.mrbeast.domain.usecase.GetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.GetShuffleModeUseCase
import com.lambao.mrbeast.domain.usecase.SetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.SetShuffleModeUseCase
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlaySongViewModel @Inject constructor(
    private val mediaPlayerManager: MediaPlayerManager,
    private val setRepeatModeUseCase: SetRepeatModeUseCase,
    private val setShuffleModeUseCase: SetShuffleModeUseCase,
    getRepeatModeUseCase: GetRepeatModeUseCase,
    getShuffleModeUseCase: GetShuffleModeUseCase,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : NetworkViewModel(ioDispatcher, defaultDispatcher),
    ISongViewModel by SongViewModel(),
    IPlaylistViewModel by PlaylistViewModel(),
    IPlaybackViewModel by PlaybackViewModel(
        getRepeatModeUseCase,
        getShuffleModeUseCase,
        setRepeatModeUseCase,
        setShuffleModeUseCase
    ) {

    private val _currentSongIndex = MutableStateFlow(-1)
    val currentSongIndex get() = _currentSongIndex.asStateFlow()

    private val _playlist = mediaPlayerManager.playlistFlow.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    init {
        mediaPlayerManager.currentIndexFlow
            .onEach { index -> _currentSongIndex.value = index }
            .launchIn(viewModelScope)
    }

    override fun getPlaylist() = _playlist

    override fun getPlaylistValue() = _playlist.value

    fun initializePlaylist(initialPlaylist: List<Song>, startIndex: Int) {
        launch {
            mediaPlayerManager.play(initialPlaylist, startIndex)
            setPlaybackEvent(PlaybackEvent.PLAY)
        }
    }

    fun previousSong() {
        launch {
            setPlaybackEvent(PlaybackEvent.PREVIOUS)
        }
    }

    fun nextSong() {
        launch {
            setPlaybackEvent(PlaybackEvent.NEXT)
        }
    }
}