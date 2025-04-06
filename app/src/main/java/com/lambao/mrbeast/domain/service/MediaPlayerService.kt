package com.lambao.mrbeast.domain.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.lambao.mrbeast.domain.model.PlaybackEvent
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.service.media_player.MediaPlayerCallBack
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast.utils.Constants.Argument.PLAYLIST
import com.lambao.mrbeast.utils.Constants.Argument.POSITION
import com.lambao.mrbeast.utils.Constants.Argument.START_INDEX
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerService : Service() {

    @Inject
    lateinit var mediaPlayerManager: MediaPlayerManager

    @Inject
    lateinit var notificationManager: MediaNotificationManager

    @Inject
    lateinit var sessionHandler: MediaSessionHandler

    override fun onCreate() {
        super.onCreate()

        sessionHandler.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() = mediaPlayerManager.resume()
            override fun onPause() = mediaPlayerManager.pause()
            override fun onSkipToNext() {
                mediaPlayerManager.next()
                updateNotification()
            }

            override fun onSkipToPrevious() {
                mediaPlayerManager.previous()
                updateNotification()
            }

            override fun onSeekTo(pos: Long) = mediaPlayerManager.seekTo(pos)
            override fun onStop() = mediaPlayerManager.stop()
        })

        mediaPlayerManager.setMediaPlayerCallback(object : MediaPlayerCallBack {
            override fun onStart() {
                sessionHandler.updateState(
                    PlaybackStateCompat.STATE_PLAYING,
                    0L,
                    mediaPlayerManager.getDuration()
                )
                mediaPlayerManager.startPositionUpdates()
            }

            override fun onComplete() {
                mediaPlayerManager.stop()
            }

            override fun onError(e: Exception) {
                mediaPlayerManager.stopPositionUpdates()
                sessionHandler.updateState(
                    PlaybackStateCompat.STATE_ERROR,
                    0L,
                    mediaPlayerManager.getDuration()
                )
            }

            override fun onPositionChanged(position: Long, duration: Long) {
                sessionHandler.updateState(
                    PlaybackStateCompat.STATE_PLAYING,
                    position,
                    mediaPlayerManager.getDuration()
                )
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { handleIntent(it) }
        updateNotification()
        return START_STICKY
    }

    private fun handleIntent(intent: Intent) {
        val command = when (intent.action) {
            PlaybackEvent.PLAY.name -> PlayCommand(
                mediaPlayerManager,
                intent.getParcelableArrayListExtra(PLAYLIST) ?: emptyList(),
                intent.getIntExtra(START_INDEX, 0)
            )

            PlaybackEvent.PAUSE.name -> PauseCommand(mediaPlayerManager)
            PlaybackEvent.RESUME.name -> ResumeCommand(mediaPlayerManager)
            PlaybackEvent.STOP.name -> StopCommand(mediaPlayerManager)
            PlaybackEvent.PREVIOUS.name -> PreviousCommand(mediaPlayerManager)
            PlaybackEvent.NEXT.name -> NextCommand(mediaPlayerManager)
            PlaybackEvent.SEEK_TO.name -> SeekToCommand(
                mediaPlayerManager,
                intent.getLongExtra(POSITION, 0L)
            )

            else -> null
        }
        command?.execute()
    }

    private fun updateNotification() {
        notificationManager.buildNotification(
            sessionHandler.getSessionToken(),
            mediaPlayerManager.currentSong
        ) { notification ->
            startForeground(Constants.Notification.NOTIFICATION_ID, notification)
        }
    }

    override fun onDestroy() {
        mediaPlayerManager.release()
        sessionHandler.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private fun startService(context: Context, intent: Intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun play(context: Context, playlist: List<Song>, startIndex: Int = 0) {
            val intent = Intent(context, MediaPlayerService::class.java).apply {
                action = PlaybackEvent.PLAY.name
                putParcelableArrayListExtra(PLAYLIST, ArrayList(playlist))
                putExtra(START_INDEX, startIndex)
            }
            startService(context, intent)
        }

        fun pause(context: Context) {
            val intent = Intent(context, MediaPlayerService::class.java).apply {
                action = PlaybackEvent.PAUSE.name
            }
            startService(context, intent)
        }

        fun resume(context: Context, position: Long? = null) {
            val intent = Intent(context, MediaPlayerService::class.java).apply {
                action = PlaybackEvent.RESUME.name
                position?.let { putExtra(POSITION, it) }
            }
            startService(context, intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, MediaPlayerService::class.java).apply {
                action = PlaybackEvent.STOP.name
            }
            startService(context, intent)
        }

        fun previous(context: Context) {
            val intent = Intent(context, MediaPlayerService::class.java).apply {
                action = PlaybackEvent.PREVIOUS.name
            }
            startService(context, intent)
        }

        fun next(context: Context) {
            val intent = Intent(context, MediaPlayerService::class.java).apply {
                action = PlaybackEvent.NEXT.name
            }
            startService(context, intent)
        }

        fun seekTo(context: Context, position: Long) {
            val intent = Intent(context, MediaPlayerService::class.java).apply {
                action = PlaybackEvent.SEEK_TO.name
                putExtra(POSITION, position)
            }
            startService(context, intent)
        }
    }
}
