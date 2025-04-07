package com.lambao.mrbeast.domain.service

import android.support.v4.media.session.PlaybackStateCompat
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.model.playback.RepeatMode
import com.lambao.mrbeast.domain.service.media_player.BaseMediaPlayer
import com.lambao.mrbeast.domain.service.media_player.MediaPlayerCallBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlayerManager @Inject constructor(
    private val mediaPlayer: BaseMediaPlayer,
    private val sessionHandler: MediaSessionHandler
) {
    private val playlist = mutableListOf<Song>()
    private var currentIndex = -1
    private var positionUpdateJob: Job? = null
    private var repeatMode = RepeatMode.NONE

    val currentSong: Song?
        get() = if (currentIndex in playlist.indices) playlist[currentIndex] else null

    fun setMediaPlayerCallback(callback: MediaPlayerCallBack) {
        mediaPlayer.setCallBack(callback)
    }

    fun setRepeatMode(mode: RepeatMode) {
        repeatMode = mode
    }

    fun play(playlist: List<Song>, startIndex: Int) {
        this.playlist.clear()
        this.playlist.addAll(playlist)
        currentIndex = startIndex.coerceIn(0, playlist.size - 1)
        playCurrentSong()
    }

    fun pause() {
        mediaPlayer.pause()
        sessionHandler.updateState(
            PlaybackStateCompat.STATE_PAUSED,
            mediaPlayer.getCurrentPosition(),
            mediaPlayer.getDuration()
        )
        stopPositionUpdates()
    }

    fun resume() {
        if (getCurrentPosition() == 0L) playCurrentSong()
        else {
            mediaPlayer.resume()
            sessionHandler.updateState(
                PlaybackStateCompat.STATE_PLAYING,
                mediaPlayer.getCurrentPosition(),
                mediaPlayer.getDuration()
            )
            startPositionUpdates()
        }
    }

    fun stop() {
        mediaPlayer.seekTo(0)
        mediaPlayer.stop()
        sessionHandler.updateState(PlaybackStateCompat.STATE_STOPPED, 0L, mediaPlayer.getDuration())
        stopPositionUpdates()
    }

    fun previous() {
        if (currentIndex > 0) {
            currentIndex--
            playCurrentSong()
        } else {
            mediaPlayer.seekTo(0)
        }
    }

    fun next() {
        if (currentIndex < playlist.size - 1) {
            currentIndex++
            playCurrentSong()
        } else {
            stop()
        }
    }

    fun seekTo(position: Long) {
        mediaPlayer.seekTo(position)
        sessionHandler.updateState(
            if (mediaPlayer.isPlaying()) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
            position,
            mediaPlayer.getDuration()
        )
    }

    fun handleComplete() {
        when (repeatMode) {
            RepeatMode.NONE -> stop()
            RepeatMode.ALL -> next()
            RepeatMode.ONE -> playCurrentSong()
        }
    }

    fun playCurrentSong() {
        if (currentIndex in playlist.indices) {
            mediaPlayer.play(playlist[currentIndex].data)
            sessionHandler.updateMetadata(playlist[currentIndex])
        }
    }

    fun startPositionUpdates() {
        stopPositionUpdates()
        positionUpdateJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive && mediaPlayer.isPlaying()) {
                sessionHandler.updateState(
                    PlaybackStateCompat.STATE_PLAYING,
                    mediaPlayer.getCurrentPosition(),
                    mediaPlayer.getDuration()
                )
                delay(1000)
            }
        }
    }

    fun stopPositionUpdates() {
        positionUpdateJob?.cancel()
    }

    fun getCurrentPosition() = mediaPlayer.getCurrentPosition()

    fun getDuration() = mediaPlayer.getDuration()

    fun release() {
        stopPositionUpdates()
        mediaPlayer.release()
    }
}