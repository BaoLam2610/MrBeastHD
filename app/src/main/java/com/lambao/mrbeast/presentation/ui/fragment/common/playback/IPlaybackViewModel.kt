package com.lambao.mrbeast.presentation.ui.fragment.common.playback

import com.lambao.mrbeast.domain.model.PlaybackEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IPlaybackViewModel {
    val playbackEvent: SharedFlow<PlaybackEvent>
    val currentDuration: StateFlow<Long>
    val currentDurationValue: Long
    fun setPlaybackEvent(event: PlaybackEvent)
    fun togglePlayPause(isPlaying: Boolean)
    fun seekTo(position: Long)
    fun setCurrentPosition(duration: Long)
}