package com.lambao.mrbeast.presentation.ui.fragment.online_songs

import com.lambao.base.data.Resource
import com.lambao.base.presentation.ui.viewmodel.network.NetworkViewModel
import com.lambao.mrbeast.di.DefaultDispatcher
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.usecase.GetOnlineSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnlineSongsViewModel @Inject constructor(
    private val getOnlineSongsUseCase: GetOnlineSongsUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : NetworkViewModel(ioDispatcher, defaultDispatcher) {

    private val _songs = MutableStateFlow<Resource<List<Song>>>(Resource.Loading())
    val songs get() = _songs.asStateFlow()

    fun getOnlineSongs() {
        collectApi(getOnlineSongsUseCase.invoke(), _songs)
    }
}