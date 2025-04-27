package com.lambao.mrbeast.presentation.ui.fragment.common.media_mode

import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
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
    private val setShuffleModeUseCase: SetShuffleModeUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider), IMediaModeViewModel {
    private val _repeatMode = MutableStateFlow(getRepeatModeUseCase.invoke())

    private val _shuffleMode = MutableStateFlow(getShuffleModeUseCase.invoke())

    override fun getRepeatMode() = _repeatMode

    override fun getRepeatModeValue() = _repeatMode.value

    override fun getShuffleMode() = _shuffleMode

    override fun getShuffleModeValue() = _shuffleMode.value

    override fun setRepeatMode(mode: RepeatMode) {
        _repeatMode.value = mode
        launch {
            setRepeatModeUseCase.invoke(mode)
        }
    }

    override fun setShuffleMode(mode: ShuffleMode) {
        _shuffleMode.value = mode
        launch {
            setShuffleModeUseCase.invoke(mode)
        }
    }

    override fun onSwitchRepeatMode() {
        when (_repeatMode.value) {
            RepeatMode.NONE -> setRepeatMode(RepeatMode.ALL)
            RepeatMode.ALL -> setRepeatMode(RepeatMode.ONE)
            RepeatMode.ONE -> setRepeatMode(RepeatMode.NONE)
        }
    }

    override fun onSwitchShuffleMode() {
        when (_shuffleMode.value) {
            ShuffleMode.OFF -> setShuffleMode(ShuffleMode.ON)
            ShuffleMode.ON -> setShuffleMode(ShuffleMode.OFF)
        }
    }

    override fun toggleRepeatMode() = Unit

    override fun toggleShuffleMode() = Unit
}