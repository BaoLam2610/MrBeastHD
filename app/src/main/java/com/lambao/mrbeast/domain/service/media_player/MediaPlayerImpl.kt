package com.lambao.mrbeast.domain.service.media_player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import java.io.IOException

class MediaPlayerImpl : BaseMediaPlayer,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnSeekCompleteListener {

    private var mediaPlayer: MediaPlayer? = null
    private var callBack: MediaPlayerCallBack? = null

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setOnPreparedListener(this@MediaPlayerImpl)
            setOnCompletionListener(this@MediaPlayerImpl)
            setOnErrorListener(this@MediaPlayerImpl)
            setOnSeekCompleteListener(this@MediaPlayerImpl)
        }
    }

    override fun play(data: String?) {
        if (data.isNullOrEmpty()) {
            callBack?.onError(MediaPlayerException("Invalid URL"))
            return
        }

        try {
            mediaPlayer?.let { player ->
                player.reset()
                player.setDataSource(data)
                player.prepareAsync()
            }
        } catch (e: IOException) {
            callBack?.onError(MediaPlayerException("Failed to load media: ${e.message}"))
        } catch (e: IllegalStateException) {
            callBack?.onError(MediaPlayerException("Media player error: ${e.message}"))
            initializePlayer()
        }
    }

    override fun pause() {
        try {
            mediaPlayer?.takeIf { it.isPlaying }?.pause()
        } catch (e: IllegalStateException) {
            callBack?.onError(MediaPlayerException("Pause failed: ${e.message}"))
        }
    }

    override fun stop() {
        try {
            mediaPlayer?.takeIf { it.isPlaying }?.stop()
            mediaPlayer?.reset()
        } catch (e: IllegalStateException) {
            callBack?.onError(MediaPlayerException("Stop failed: ${e.message}"))
        }
    }

    override fun seekTo(position: Int) {
        try {
            mediaPlayer?.seekTo(position)
        } catch (e: IllegalStateException) {
            callBack?.onError(MediaPlayerException("Seek failed: ${e.message}"))
        }
    }

    override fun isPlaying(): Boolean {
        return try {
            mediaPlayer?.isPlaying == true
        } catch (e: IllegalStateException) {
            false
        }
    }

    override fun setCallBack(callBack: MediaPlayerCallBack) {
        this.callBack = callBack
        startPositionUpdate() // Bắt đầu cập nhật vị trí khi có callback
    }

    override fun release() {
        stopPositionUpdate()
        mediaPlayer?.release()
        mediaPlayer = null
        callBack = null
    }

    // Theo dõi vị trí phát
    private val handler = Handler(Looper.getMainLooper())
    private val updatePositionRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    callBack?.onPositionChanged(it.currentPosition, it.duration)
                }
            }
            handler.postDelayed(this, 1000) // Cập nhật mỗi giây
        }
    }

    private fun startPositionUpdate() {
        handler.post(updatePositionRunnable)
    }

    private fun stopPositionUpdate() {
        handler.removeCallbacks(updatePositionRunnable)
    }

    // MediaPlayer Listeners
    override fun onPrepared(mp: MediaPlayer) {
        try {
            mp.start()
            callBack?.onStart()
        } catch (e: IllegalStateException) {
            callBack?.onError(MediaPlayerException("Start failed: ${e.message}"))
        }
    }

    override fun onCompletion(mp: MediaPlayer) {
        callBack?.onComplete()
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        callBack?.onError(MediaPlayerException("Playback error - what: $what, extra: $extra"))
        initializePlayer()
        return true
    }

    override fun onSeekComplete(mp: MediaPlayer) {
        callBack?.onPositionChanged(mp.currentPosition, mp.duration)
    }
}