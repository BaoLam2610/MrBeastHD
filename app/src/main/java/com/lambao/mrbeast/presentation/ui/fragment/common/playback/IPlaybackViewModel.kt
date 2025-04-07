package com.lambao.mrbeast.presentation.ui.fragment.common.playback

import com.lambao.mrbeast.domain.model.PlaybackEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IPlaybackViewModel {
    fun getPlaybackEvent(): SharedFlow<PlaybackEvent>
    fun getCurrentDuration(): StateFlow<Long>
    fun getCurrentDurationValue(): Long
    fun setPlaybackEvent(event: PlaybackEvent)
    fun togglePlayPause(isPlaying: Boolean)
    fun seekTo(position: Long)
    fun setCurrentPosition(duration: Long)
}