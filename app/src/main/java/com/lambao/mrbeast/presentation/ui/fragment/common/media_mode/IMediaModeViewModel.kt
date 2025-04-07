package com.lambao.mrbeast.presentation.ui.fragment.common.media_mode

import com.lambao.mrbeast.domain.model.playback.RepeatMode
import com.lambao.mrbeast.domain.model.playback.ShuffleMode
import kotlinx.coroutines.flow.StateFlow

interface IMediaModeViewModel {
    fun getRepeatMode(): StateFlow<RepeatMode>
    fun getShuffleMode(): StateFlow<ShuffleMode>
    fun setRepeatModeAsync(mode: RepeatMode)
    fun setShuffleModeAsync(mode: ShuffleMode)
    fun toggleRepeatMode()
    fun toggleShuffleMode()
}