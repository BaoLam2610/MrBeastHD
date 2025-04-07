package com.lambao.mrbeast.presentation.ui.fragment.common.media_mode

import com.lambao.mrbeast.domain.model.playback.RepeatMode
import com.lambao.mrbeast.domain.model.playback.ShuffleMode
import kotlinx.coroutines.flow.StateFlow

interface IMediaModeViewModel {
    fun getRepeatMode(): StateFlow<RepeatMode>
    fun getRepeatModeValue(): RepeatMode
    fun getShuffleMode(): StateFlow<ShuffleMode>
    fun getShuffleModeValue(): ShuffleMode
    fun setRepeatMode(mode: RepeatMode)
    fun setShuffleMode(mode: ShuffleMode)
    fun onSwitchRepeatMode()
    fun onSwitchShuffleMode()
    fun toggleRepeatMode()
    fun toggleShuffleMode()
}