package com.lambao.mrbeast.presentation.ui.fragment.common.playback

import com.lambao.mrbeast.domain.model.playback.PlaybackEvent
import com.lambao.mrbeast.presentation.ui.fragment.common.media_duration.IMediaDurationViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.media_mode.IMediaModeViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.song.ISongViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IPlaybackViewModel : ISongViewModel, IMediaDurationViewModel, IMediaModeViewModel {
    fun getPlaybackEvent(): SharedFlow<PlaybackEvent>
    fun setPlaybackEvent(event: PlaybackEvent)
    fun togglePlayPause()
    fun previous()
    fun next()
    fun seekTo(position: Long)
    fun isPlaying(): StateFlow<Boolean>
}