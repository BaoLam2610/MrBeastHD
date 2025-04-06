package com.lambao.mrbeast.presentation.ui.fragment.common.playback

import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.mrbeast.domain.model.PlaybackEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow


class PlaybackViewModel : BaseViewModel(), IPlaybackViewModel {
    private val _playbackEvent = MutableSharedFlow<PlaybackEvent>()
    override val playbackEvent get() = _playbackEvent

    private val _currentDuration = MutableStateFlow(0L)
    override val currentDuration get() = _currentDuration
    override val currentDurationValue get() = _currentDuration.value

    override fun setPlaybackEvent(event: PlaybackEvent) {
        launch { _playbackEvent.emit(event) }
    }

    override fun togglePlayPause(isPlaying: Boolean) {
        setPlaybackEvent(
            if (isPlaying) PlaybackEvent.PAUSE
            else PlaybackEvent.RESUME
        )
    }

    override fun seekTo(position: Long) {
        setCurrentPosition(position)
        setPlaybackEvent(PlaybackEvent.SEEK_TO)
    }

    override fun setCurrentPosition(duration: Long) {
        _currentDuration.value = duration
    }
}