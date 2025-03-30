package com.lambao.mrbeast.presentation.ui.fragment.offline_songs

import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.mrbeast.di.DefaultDispatcher
import com.lambao.mrbeast.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class OfflineSongsViewModel @Inject constructor(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : BaseViewModel(ioDispatcher, defaultDispatcher) {
}