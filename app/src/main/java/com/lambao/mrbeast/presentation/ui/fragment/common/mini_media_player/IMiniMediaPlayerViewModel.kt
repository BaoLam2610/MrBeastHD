package com.lambao.mrbeast.presentation.ui.fragment.common.mini_media_player

import com.lambao.mrbeast.presentation.ui.fragment.common.playback.IPlaybackViewModel
import kotlinx.coroutines.flow.StateFlow

interface IMiniMediaPlayerViewModel : IPlaybackViewModel {
    fun shouldShowMiniPlayer(): StateFlow<Boolean>
    fun setShouldShowMiniPlayer(isShow: Boolean)
}