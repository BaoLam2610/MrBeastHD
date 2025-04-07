package com.lambao.mrbeast.presentation.ui.fragment.common.playback

import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.mrbeast.domain.model.playback.PlaybackEvent
import com.lambao.mrbeast.domain.usecase.GetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.GetShuffleModeUseCase
import com.lambao.mrbeast.domain.usecase.SetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.SetShuffleModeUseCase
import com.lambao.mrbeast.presentation.ui.fragment.common.media_mode.IMediaModeViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.media_mode.MediaModeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class PlaybackViewModel @Inject constructor(
    getRepeatModeUseCase: GetRepeatModeUseCase,
    getShuffleModeUseCase: GetShuffleModeUseCase,
    setRepeatModeUseCase: SetRepeatModeUseCase,
    setShuffleModeUseCase: SetShuffleModeUseCase,
) : BaseViewModel(), IPlaybackViewModel,
    IMediaModeViewModel by MediaModeViewModel(
        getRepeatModeUseCase,
        getShuffleModeUseCase,
        setRepeatModeUseCase,
        setShuffleModeUseCase
    ) {

    private val _playbackEvent = MutableStateFlow(PlaybackEvent.INIT)

    private val _currentDuration = MutableStateFlow(0L)

    override fun getPlaybackEvent() = _playbackEvent

    override fun getCurrentDuration() = _currentDuration

    override fun getCurrentDurationValue() = _currentDuration.value

    override fun setPlaybackEvent(event: PlaybackEvent) {
        launch { _playbackEvent.emit(event) }
    }

    override fun togglePlayPause(isPlaying: Boolean) {
        setPlaybackEvent(
            if (isPlaying) PlaybackEvent.PAUSE
            else PlaybackEvent.RESUME
        )
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
        setCurrentPosition(position)
        setPlaybackEvent(PlaybackEvent.SEEK_TO)
    }

    override fun setCurrentPosition(duration: Long) {
        _currentDuration.value = duration
    }
}