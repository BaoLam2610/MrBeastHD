package com.lambao.mrbeast.presentation.ui.fragment.common.mini_media_player

import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.mrbeast.domain.usecase.GetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.GetShuffleModeUseCase
import com.lambao.mrbeast.domain.usecase.SetRepeatModeUseCase
import com.lambao.mrbeast.domain.usecase.SetShuffleModeUseCase
import com.lambao.mrbeast.presentation.ui.fragment.common.playback.IPlaybackViewModel
import com.lambao.mrbeast.presentation.ui.fragment.common.playback.PlaybackViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MiniMediaPlayerViewModel @Inject constructor(
    getRepeatModeUseCase: GetRepeatModeUseCase,
    getShuffleModeUseCase: GetShuffleModeUseCase,
    setRepeatModeUseCase: SetRepeatModeUseCase,
    setShuffleModeUseCase: SetShuffleModeUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider), IMiniMediaPlayerViewModel,
    IPlaybackViewModel by PlaybackViewModel(
        getRepeatModeUseCase,
        getShuffleModeUseCase,
        setRepeatModeUseCase,
        setShuffleModeUseCase,
        dispatcherProvider
    ) {

    private val _shouldShowMiniPlayer = MutableStateFlow(false)

    override fun shouldShowMiniPlayer() = _shouldShowMiniPlayer

    override fun setShouldShowMiniPlayer(isShow: Boolean) {
        _shouldShowMiniPlayer.value = isShow
    }
}