package com.lambao.mrbeast.presentation.ui.fragment.online_playlist

import androidx.lifecycle.viewModelScope
import com.lambao.base.data.map
import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import com.lambao.base.presentation.ui.state.ScreenState
import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.usecase.GetOnlinePlaylistUseCase
import com.lambao.mrbeast.domain.usecase.GetOnlineSongInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OnlinePlaylistViewModel @Inject constructor(
    private val getOnlinePlaylistUseCase: GetOnlinePlaylistUseCase,
    private val getOnlineSongInfoUseCase: GetOnlineSongInfoUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    private val _shouldFetchInfo = MutableStateFlow(true)
    val shouldFetchInfo get() = _shouldFetchInfo.asStateFlow()

    private val _playlist = MutableStateFlow<List<Song>>(emptyList())
    val playlist get() = _playlist.asStateFlow()
    val playlistValue get() = _playlist.value

    private val _shouldShowEmptyData = combine(
        screenState,
        _playlist
    ) { screenState, songs ->
        if (screenState is ScreenState.Success) {
            songs.isEmpty()
        } else {
            false
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val shouldShowEmptyData get() = _shouldShowEmptyData

    fun getOnlinePlaylist() {
        if (!_shouldFetchInfo.value) return
        handleData(getOnlinePlaylistUseCase.invoke()) {
            fetchAllSongInfo(it)
        }
    }

    fun fetchAllSongInfo(dataPlaylist: List<Song>) {
        if (dataPlaylist.isEmpty()) return

        if (!_shouldFetchInfo.value) return

        val flows = dataPlaylist.map { song ->
            getOnlineSongInfoUseCase.invoke(song.link).map { resource ->
                resource.map { Pair(song, it) }
            }
        }
        handleMultiData(
            *flows.toTypedArray(),
        ) { results ->
            _shouldFetchInfo.value = false
            val updatedSongs = dataPlaylist.mapIndexed { index, song ->
                val songInfoPair = results.getOrNull(index)
                if (songInfoPair != null && songInfoPair.first.id == song.id) {
                    song.copy(
                        data = songInfoPair.second.mp3Url,
                        thumbnail = songInfoPair.second.thumbnail
                    )
                } else {
                    song
                }
            }
            launch {
                _playlist.emit(updatedSongs)
            }
        }
    }
}