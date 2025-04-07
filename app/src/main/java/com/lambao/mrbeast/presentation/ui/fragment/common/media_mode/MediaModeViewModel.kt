package com.lambao.mrbeast.presentation.ui.fragment.common.media_mode

import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.mrbeast.domain.model.playback.RepeatMode
import com.lambao.mrbeast.domain.model.playback.ShuffleMode
import com.lambao.mrbeast.domain.usecase.GetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.GetShuffleModeUseCase
import com.lambao.mrbeast.domain.usecase.SetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.SetShuffleModeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MediaModeViewModel @Inject constructor(
    getRepeatModeUseCase: GetRepeatModeUseCase,
    getShuffleModeUseCase: GetShuffleModeUseCase,
    private val setRepeatModeUseCase: SetRepeatModeUseCase,
    private val setShuffleModeUseCase: SetShuffleModeUseCase
) : BaseViewModel(), IMediaModeViewModel {
    private val _repeatMode = MutableStateFlow(getRepeatModeUseCase.invoke())

    private val _shuffleMode = MutableStateFlow(getShuffleModeUseCase.invoke())

    override fun getRepeatMode() = _repeatMode

    override fun getShuffleMode() = _shuffleMode

    override fun setRepeatModeAsync(mode: RepeatMode) {
        launch {
            setRepeatModeUseCase.invoke(mode)
            _repeatMode.emit(mode)
        }
    }

    override fun setShuffleModeAsync(mode: ShuffleMode) {
        launch {
            setShuffleModeUseCase.invoke(mode)
            _shuffleMode.emit(mode)
        }
    }

    override fun toggleRepeatMode() {
        when (_repeatMode.value) {
            RepeatMode.NONE -> setRepeatModeAsync(RepeatMode.ALL)
            RepeatMode.ALL -> setRepeatModeAsync(RepeatMode.ONE)
            RepeatMode.ONE -> setRepeatModeAsync(RepeatMode.NONE)
        }
    }

    override fun toggleShuffleMode() {
        when (_shuffleMode.value) {
            ShuffleMode.OFF -> setShuffleModeAsync(ShuffleMode.ON)
            ShuffleMode.ON -> setShuffleModeAsync(ShuffleMode.OFF)
        }
    }
}