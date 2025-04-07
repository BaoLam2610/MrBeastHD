package com.lambao.mrbeast.presentation.ui.activity

import androidx.lifecycle.viewModelScope
import com.lambao.base.presentation.ui.viewmodel.network.NetworkViewModel
import com.lambao.mrbeast.di.DefaultDispatcher
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.service.MediaPlayerManager
import com.lambao.mrbeast.domain.usecase.GetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.GetShuffleModeUseCase
import com.lambao.mrbeast.domain.usecase.SetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.SetShuffleModeUseCase
import com.lambao.mrbeast.presentation.ui.fragment.common.mini_media_player.IMiniMediaPlayerViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.mini_media_player.MiniMediaPlayerViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.playlist.IPlaylistViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.playlist.PlaylistViewModel
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
class MusicViewModel @Inject constructor(
    private val mediaPlayerManager: MediaPlayerManager,
    getRepeatModeUseCase: GetRepeatModeUseCase,
    getShuffleModeUseCase: GetShuffleModeUseCase,
    setRepeatModeUseCase: SetRepeatModeUseCase,
    setShuffleModeUseCase: SetShuffleModeUseCase,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : NetworkViewModel(ioDispatcher, defaultDispatcher),
    IPlaylistViewModel by PlaylistViewModel(),
    IMiniMediaPlayerViewModel by MiniMediaPlayerViewModel(
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
        }
    }
}