package com.lambao.mrbeast.presentation.ui.fragment.common.media_duration

import kotlinx.coroutines.flow.StateFlow

interface IMediaDurationViewModel {
    fun getCurrentDuration(): StateFlow<Long>
    fun getCurrentDurationValue(): Long
    fun getCurrentDurationInt(): StateFlow<Int>
    fun getCurrentDurationTime(): StateFlow<String>

    fun getTotalDuration(): StateFlow<Long>
    fun getTotalDurationValue(): Long
    fun getTotalDurationInt(): StateFlow<Int>
    fun getTotalDurationTime(): StateFlow<String>

    fun setCurrentDuration(duration: Long)
    fun setTotalDuration(duration: Long)
}