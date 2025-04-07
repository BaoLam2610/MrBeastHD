package com.lambao.mrbeast.presentation.ui.fragment.common.media_duration

import androidx.lifecycle.viewModelScope
import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.mrbeast.extension.toTimeString
import com.lambao.mrbeast.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MediaDurationViewModel : BaseViewModel(), IMediaDurationViewModel {

    private val _currentDuration = MutableStateFlow(0L)

    private val _currentDurationInt = _currentDuration.map {
        it.toInt()
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    private val _currentDurationTime = _currentDuration.map {
        (it / 1000).toTimeString()
    }.stateIn(viewModelScope, SharingStarted.Lazily, Constants.Media.DEFAULT_TIME)

    private val _totalDuration = MutableStateFlow(0L)

    private val _totalDurationInt = _totalDuration.map {
        it.toInt()
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    private val _totalDurationTime = _totalDuration.map {
        (it / 1000).toTimeString()
    }.stateIn(viewModelScope, SharingStarted.Lazily, Constants.Media.DEFAULT_TIME)

    override fun getCurrentDuration() = _currentDuration

    override fun getCurrentDurationValue() = _currentDuration.value

    override fun getCurrentDurationInt() = _currentDurationInt

    override fun getCurrentDurationTime() = _currentDurationTime

    override fun getTotalDuration() = _totalDuration

    override fun getTotalDurationValue() = _totalDuration.value

    override fun getTotalDurationInt() = _totalDurationInt

    override fun getTotalDurationTime() = _totalDurationTime

    override fun setCurrentDuration(duration: Long) {
        _currentDuration.value = duration
    }

    override fun setTotalDuration(duration: Long) {
        _totalDuration.value = duration
    }
}