package com.lambao.mrbeast.presentation.ui.fragment.play_song

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.lambao.base.presentation.ui.viewmodel.network.NetworkViewModel
import com.lambao.mrbeast.di.DefaultDispatcher
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.usecase.GetIndexInPlaylistUseCase
import com.lambao.mrbeast.domain.usecase.GetOnlineSongInfoUseCase
import com.lambao.mrbeast_music.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlaySongViewModel @Inject constructor(
    private val getIndexInPlaylistUseCase: GetIndexInPlaylistUseCase,
    private val getOnlineSongInfoUseCase: GetOnlineSongInfoUseCase,
    @ApplicationContext private val context: Context,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : NetworkViewModel(ioDispatcher, defaultDispatcher) {

    private val _song = MutableStateFlow<Song?>(null)
    val song get() = _song.asStateFlow()

    private val _songList = MutableStateFlow<List<Song>>(emptyList())
    val songList get() = _songList.asStateFlow()

    private val _currentSongIndex = MutableStateFlow(-1)
    val currentSongIndex get() = _currentSongIndex.asStateFlow()

    private val _currentDuration = MutableStateFlow(0L)
    val currentDuration get() = _currentDuration.asStateFlow()

    private val _combineIndexInPlaylist = combine(
        _song,
        _songList
    ) { song, playlist ->
        getIndexInPlaylistUseCase.invoke(song, playlist)
    }.stateIn(viewModelScope, SharingStarted.Lazily, -1)
    val combineIndexInPlaylist get() = _combineIndexInPlaylist

    private val _shouldPlaySong = MutableSharedFlow<Unit>()
    val shouldPlaySong get() = _shouldPlaySong.asSharedFlow()

    fun setSong(song: Song?) {
        _song.value = song
    }

    fun setSongAsync(song: Song) {
        launch {
            _song.emit(song)
        }
    }

    fun setSongList(songList: List<Song>) {
        _songList.value = songList
    }

    fun setCurrentSongIndex(index: Int) {
        launch {
            _currentSongIndex.emit(index)
        }
    }

    fun setCurrentDuration(duration: Long) {
        launch {
            _currentDuration.emit(duration)
        }
    }

    fun setShouldPlaySong() {
        launch {
            _shouldPlaySong.emit(Unit)
        }
    }

    fun previousSong() {
        if (currentSongIndex.value > 0) {
            setCurrentSongIndex(currentSongIndex.value - 1)
        }
    }

    fun nextSong() {
        if (currentSongIndex.value < songList.value.size - 1) {
            setCurrentSongIndex(currentSongIndex.value + 1)
        }
    }

    fun getSongInfo() {
        val currentIndex = _currentSongIndex.value
        if (currentIndex < 0 || currentIndex >= _songList.value.size) return

        collectApi(
            getOnlineSongInfoUseCase.invoke(_songList.value[currentIndex].link)
        ) { onlineSongInfo ->
            val updatedSong = _songList.value[currentIndex].copy(
                data = onlineSongInfo.mp3Url,
                thumbnail = onlineSongInfo.thumbnail
            )

            updatedSong.let {
                _song.value = it
                _songList.value = _songList.value.mapIndexed { index, song ->
                    if (index == currentIndex) it else song
                }
                setShouldPlaySong() // Phát tín hiệu để Fragment gọi Service
            }
        }
    }
}