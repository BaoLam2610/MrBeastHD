package com.lambao.mrbeast.domain.service.media_player

interface BaseMediaPlayer {
    fun play(data: String?)
    fun resume()
    fun pause()
    fun stop()
    fun seekTo(position: Long)
    fun isPlaying(): Boolean
    fun setCallBack(callBack: MediaPlayerCallBack)
    fun release()
    fun getCurrentPosition(): Long
    fun getDuration(): Long
}

