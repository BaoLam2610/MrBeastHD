package com.lambao.mrbeast.presentation.ui.fragment.play_song

import com.lambao.base.presentation.ui.viewmodel.network.NetworkViewModel
import com.lambao.mrbeast.di.DefaultDispatcher
import com.lambao.mrbeast.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class PlaySongViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : NetworkViewModel() {
}