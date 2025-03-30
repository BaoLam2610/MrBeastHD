package com.lambao.mrbeast.domain.service.media_player

import android.media.MediaPlayer
import java.io.IOException

class MediaPlayerImpl : BaseMediaPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var callBack: MediaPlayerCallBack? = null

    init {
        mediaPlayer = MediaPlayer()
    }

    override fun play(data: String?) {
        if (data == null) {
            callBack?.onError(MediaPlayerException("Media data is null"))
            return
        }
        try {
            mediaPlayer?.let { player ->
                player.reset()
                player.setDataSource(data)
                player.setOnPreparedListener {
                    it.start()
                    callBack?.onStart()
                }
                player.setOnCompletionListener {
                    callBack?.onComplete()
                }
                player.setOnErrorListener { _, what, extra ->
                    callBack?.onError(MediaPlayerException("MediaPlayer error: what=$what, extra=$extra"))
                    false
                }
                player.prepareAsync()
            }
        } catch (e: IOException) {
            callBack?.onError(MediaPlayerException("Failed to load media: ${e.message}"))
        } catch (e: IllegalStateException) {
            callBack?.onError(MediaPlayerException("MediaPlayer state error: ${e.message}"))
        }
    }

    override fun resume() {
        try {
            mediaPlayer?.takeIf { !it.isPlaying }?.start()
        } catch (e: IllegalStateException) {
            callBack?.onError(MediaPlayerException("Error resuming media: ${e.message}"))
        }
    }

    override fun pause() {
        try {
            mediaPlayer?.takeIf { it.isPlaying }?.pause()
        } catch (e: IllegalStateException) {
            callBack?.onError(MediaPlayerException("Error pausing media: ${e.message}"))
        }
    }

    override fun stop() {
        try {
            mediaPlayer?.stop()
        } catch (e: IllegalStateException) {
            callBack?.onError(MediaPlayerException("Error stopping media: ${e.message}"))
        }
    }

    override fun seekTo(position: Long) {
        try {
            mediaPlayer?.seekTo(position.toInt())
            callBack?.onPositionChanged(position, getDuration())
        } catch (e: IllegalStateException) {
            callBack?.onError(MediaPlayerException("Error seeking media: ${e.message}"))
        }
    }

    override fun isPlaying(): Boolean {
        return try {
            mediaPlayer?.isPlaying == true
        } catch (e: IllegalStateException) {
            callBack?.onError(MediaPlayerException("Error checking play state: ${e.message}"))
            false
        }
    }

    override fun setCallBack(callBack: MediaPlayerCallBack) {
        this.callBack = callBack
    }

    override fun release() {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
            callBack = null
        } catch (e: Exception) {
            callBack?.onError(MediaPlayerException("Error releasing media player: ${e.message}"))
        }
    }

    override fun getCurrentPosition(): Long = mediaPlayer?.currentPosition?.toLong() ?: 0

    override fun getDuration(): Long = mediaPlayer?.duration?.toLong() ?: 0
}