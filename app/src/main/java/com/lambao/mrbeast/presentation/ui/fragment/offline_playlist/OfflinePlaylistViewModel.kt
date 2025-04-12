package com.lambao.mrbeast.presentation.ui.fragment.offline_playlist

import androidx.lifecycle.viewModelScope
import com.lambao.base.presentation.ui.state.ScreenState
import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.mrbeast.di.DefaultDispatcher
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.di.MainDispatcher
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.usecase.GetOfflinePlaylistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OfflinePlaylistViewModel @Inject constructor(
    private val getOfflinePlaylistUseCase: GetOfflinePlaylistUseCase,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
) : BaseViewModel(ioDispatcher, defaultDispatcher, mainDispatcher) {
    private val _shouldFetchInfo = MutableStateFlow(true)
    val shouldFetchInfo get() = _shouldFetchInfo.asStateFlow()

    private val _playlist = MutableStateFlow<List<Song>>(emptyList())
    val playlist get() = _playlist.asStateFlow()
    val playlistValue get() = _playlist.value

    private val _shouldShowEmptyData = combine(
        screenState,
        _playlist
    ) { screenState, songs ->
        if (screenState is ScreenState.Success || screenState is ScreenState.Error) {
            songs.isEmpty()
        } else {
            false
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val shouldShowEmptyData get() = _shouldShowEmptyData

    fun getOfflinePlaylist() {
        if (!_shouldFetchInfo.value) return
        handleData(getOfflinePlaylistUseCase.invoke()) {
            launch {
                _shouldFetchInfo.emit(false)
                _playlist.emit(it)
            }
        }
    }
}