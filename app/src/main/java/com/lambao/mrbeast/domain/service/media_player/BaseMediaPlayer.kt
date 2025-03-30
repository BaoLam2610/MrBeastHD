package com.lambao.mrbeast.domain.service.media_player

interface BaseMediaPlayer {
    fun play(data: String?)
    fun pause()
    fun stop()
    fun seekTo(position: Int)
    fun isPlaying(): Boolean
    fun setCallBack(callBack: MediaPlayerCallBack)
    fun release()
}

