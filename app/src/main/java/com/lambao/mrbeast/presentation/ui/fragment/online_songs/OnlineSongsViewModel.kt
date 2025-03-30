package com.lambao.mrbeast.presentation.ui.fragment.online_songs

import androidx.lifecycle.viewModelScope
import com.lambao.base.presentation.ui.state.ScreenState
import com.lambao.base.presentation.ui.viewmodel.network.NetworkViewModel
import com.lambao.mrbeast.di.DefaultDispatcher
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.usecase.GetOnlineSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OnlineSongsViewModel @Inject constructor(
    private val getOnlineSongsUseCase: GetOnlineSongsUseCase,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : NetworkViewModel(ioDispatcher, defaultDispatcher) {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs get() = _songs.asStateFlow()

    private val _shouldShowEmptyData = combine(
        screenState,
        _songs
    ) { screenState, songs ->
        if (screenState is ScreenState.Success) {
            songs.isEmpty()
        } else {
            false
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val shouldShowEmptyData: StateFlow<Boolean> get() = _shouldShowEmptyData

    fun getOnlineSongs() {
        collectApi(getOnlineSongsUseCase.invoke()) { it ->
            _songs.emit(it)
        }
    }
}