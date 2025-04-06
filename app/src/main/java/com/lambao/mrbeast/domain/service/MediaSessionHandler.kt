package com.lambao.mrbeast.domain.service

import android.content.Context
import android.content.Intent
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.utils.Constants.Argument.DURATION
import com.lambao.mrbeast.utils.Constants.Argument.POSITION
import com.lambao.mrbeast.utils.Constants.Argument.SONG
import com.lambao.mrbeast.utils.Constants.Argument.STATE
import com.lambao.mrbeast.utils.Constants.Broadcast.ACTION_METADATA_CHANGED
import com.lambao.mrbeast.utils.Constants.Broadcast.ACTION_PLAYBACK_STATE_CHANGED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaSessionHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val mediaSession =
        MediaSessionCompat(context, MediaPlayerService::class.java.simpleName).apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            isActive = true
        }

    fun setCallback(callback: MediaSessionCompat.Callback) {
        mediaSession.setCallback(callback)
    }

    fun updateState(state: Int, position: Long = 0L, duration: Long = 0L) {
        val playbackState = PlaybackStateCompat.Builder()
            .setState(
                state,
                position,
                if (state == PlaybackStateCompat.STATE_PLAYING) 1.0f else 0.0f
            )
            .setActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_STOP or
                        PlaybackStateCompat.ACTION_SEEK_TO
            )
            .build()
        mediaSession.setPlaybackState(playbackState)
        val metadata = MediaMetadataCompat.Builder()
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
            .build()
        mediaSession.setMetadata(metadata)
        sendPlaybackStateBroadcast(state, position, duration)
    }

    fun updateMetadata(song: Song?) {
        sendMetadataChangedBroadcast(song)
    }

    private fun sendPlaybackStateBroadcast(state: Int, position: Long, duration: Long) {
        val intent = Intent(ACTION_PLAYBACK_STATE_CHANGED).apply {
            putExtra(STATE, state)
            putExtra(POSITION, position)
            putExtra(DURATION, duration)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun sendMetadataChangedBroadcast(song: Song?) {
        val intent = Intent(ACTION_METADATA_CHANGED).apply {
            putExtra(SONG, song)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    fun release() {
        mediaSession.release()
    }

    fun getSessionToken(): MediaSessionCompat.Token = mediaSession.sessionToken
}