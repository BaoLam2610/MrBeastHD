package com.lambao.mrbeast.presentation.ui.fragment.common.playback

import androidx.lifecycle.viewModelScope
import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.mrbeast.domain.model.playback.PlaybackEvent
import com.lambao.mrbeast.domain.usecase.GetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.GetShuffleModeUseCase
import com.lambao.mrbeast.domain.usecase.SetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.SetShuffleModeUseCase
import com.lambao.mrbeast.presentation.ui.fragment.common.media_duration.IMediaDurationViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.media_duration.MediaDurationViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.media_mode.IMediaModeViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.media_mode.MediaModeViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.song.ISongViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.song.SongViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


class PlaybackViewModel @Inject constructor(
    getRepeatModeUseCase: GetRepeatModeUseCase,
    getShuffleModeUseCase: GetShuffleModeUseCase,
    setRepeatModeUseCase: SetRepeatModeUseCase,
    setShuffleModeUseCase: SetShuffleModeUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider), IPlaybackViewModel,
    ISongViewModel by SongViewModel(dispatcherProvider),
    IMediaDurationViewModel by MediaDurationViewModel(dispatcherProvider),
    IMediaModeViewModel by MediaModeViewModel(
        getRepeatModeUseCase,
        getShuffleModeUseCase,
        setRepeatModeUseCase,
        setShuffleModeUseCase,
        dispatcherProvider
    ) {
    private val _playbackEvent = MutableSharedFlow<PlaybackEvent>()

    private val _isPlaying = _playbackEvent.map {
        it != PlaybackEvent.STOP && it != PlaybackEvent.PAUSE
    }.stateIn(viewModelScope, SharingStarted.Lazily, true)

    override fun getPlaybackEvent() = _playbackEvent

    override fun setPlaybackEvent(event: PlaybackEvent) {
        launch { _playbackEvent.emit(event) }
    }

    override fun togglePlayPause() {
        setPlaybackEvent(
            if (_isPlaying.value) PlaybackEvent.PAUSE
            else PlaybackEvent.RESUME
        )
    }

    override fun previous() {
        setPlaybackEvent(PlaybackEvent.PREVIOUS)
    }

    override fun next() {
        setPlaybackEvent(PlaybackEvent.NEXT)
    }

    override fun toggleRepeatMode() {
        onSwitchRepeatMode()
        setPlaybackEvent(PlaybackEvent.REPEAT)
    }

    override fun toggleShuffleMode() {
        onSwitchShuffleMode()
        setPlaybackEvent(PlaybackEvent.SHUFFLE)
    }

    override fun seekTo(position: Long) {
        setCurrentDuration(position)
        setPlaybackEvent(PlaybackEvent.SEEK_TO)
    }

    override fun isPlaying() = _isPlaying
}