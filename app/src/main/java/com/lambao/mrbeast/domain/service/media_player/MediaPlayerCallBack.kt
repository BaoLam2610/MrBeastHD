package com.lambao.mrbeast.domain.service.media_player

interface MediaPlayerCallBack {
    fun onStart()
    fun onComplete()
    fun onError(e: Exception)
    fun onPositionChanged(position: Long, duration: Long)
}