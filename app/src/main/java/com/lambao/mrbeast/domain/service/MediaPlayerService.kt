package com.lambao.mrbeast.domain.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media.app.NotificationCompat.MediaStyle
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.service.media_player.BaseMediaPlayer
import com.lambao.mrbeast.domain.service.media_player.MediaPlayerCallBack
import com.lambao.mrbeast.domain.service.media_player.MediaPlayerImpl
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast.utils.Constants.Argument.DURATION
import com.lambao.mrbeast.utils.Constants.Argument.POSITION
import com.lambao.mrbeast.utils.Constants.Argument.SONG
import com.lambao.mrbeast.utils.Constants.Argument.SONGS
import com.lambao.mrbeast.utils.Constants.Argument.START_INDEX
import com.lambao.mrbeast.utils.Constants.Argument.STATE
import com.lambao.mrbeast.utils.Constants.Broadcast.ACTION_METADATA_CHANGED
import com.lambao.mrbeast.utils.Constants.Broadcast.ACTION_PLAYBACK_STATE_CHANGED
import com.lambao.mrbeast_music.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MediaPlayerService : Service() {
    private lateinit var mediaPlayer: BaseMediaPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private var positionUpdateJob: Job? = null
    private val playlist: MutableList<Song> = mutableListOf()
    private var currentSongIndex: Int = -1

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayerImpl()
        createNotificationChannel()
        setupMediaSession()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.MediaAction.PLAY -> {
                val songs = intent.getParcelableArrayListExtra<Song>(SONGS)
                val startIndex = intent.getIntExtra(START_INDEX, 0)
                if (songs != null && songs.isNotEmpty()) {
                    playlist.clear()
                    playlist.addAll(songs)
                    currentSongIndex = startIndex.coerceIn(0, playlist.size - 1)
                    handlePlay(playlist[currentSongIndex])
                }
            }

            Constants.MediaAction.PAUSE -> {
                handlePause()
            }

            Constants.MediaAction.RESUME -> handleResume()
            Constants.MediaAction.STOP -> handleStop()
            Constants.MediaAction.PREVIOUS -> handlePrevious()
            Constants.MediaAction.NEXT -> handleNext()
            Constants.MediaAction.SEEK_TO -> {
                val position = intent.getLongExtra(Constants.Argument.POSITION, 0L)
                handleSeekTo(position)
            }
        }
        updateNotification()
        return START_STICKY
    }

    private fun sendPlaybackStateBroadcast(state: Int, position: Long, duration: Long) {
        val intent = Intent(ACTION_PLAYBACK_STATE_CHANGED).apply {
            putExtra(STATE, state)
            putExtra(POSITION, position)
            putExtra(DURATION, duration)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun sendMetadataChangedBroadcast(song: Song?) {
        val intent = Intent(ACTION_METADATA_CHANGED).apply {
            putExtra(SONG, song)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                Constants.Notification.CHANNEL_ID,
                Constants.Notification.CHANNEL_NAME,
                android.app.NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(android.app.NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun setupMediaSession() {
        mediaSession = MediaSessionCompat(this, this::class.java.simpleName).apply {
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    handleResume()
                }

                override fun onPause() {
                    handlePause()
                }

                override fun onSkipToNext() {
                    handleNext()
                }

                override fun onSkipToPrevious() {
                    handlePrevious()
                }

                override fun onSeekTo(pos: Long) {
                    handleSeekTo(pos)
                }

                override fun onStop() {
                    handleStop()
                }
            })
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            isActive = true
        }

        mediaPlayer.setCallBack(object : MediaPlayerCallBack {
            override fun onStart() {
                updatePlaybackState(
                    PlaybackStateCompat.STATE_PLAYING,
                    0L,
                    mediaPlayer.getDuration()
                )
                startPositionUpdates()
            }

            override fun onComplete() {
                updatePlaybackState(PlaybackStateCompat.STATE_STOPPED)
                stopPositionUpdates()
                stopForeground(STOP_FOREGROUND_REMOVE)
            }

            override fun onError(e: Exception) {
                updatePlaybackState(PlaybackStateCompat.STATE_ERROR)
                stopPositionUpdates()
            }

            override fun onPositionChanged(position: Long, duration: Long) {
                updatePlaybackState(PlaybackStateCompat.STATE_PLAYING, position, duration)
            }
        })
    }

    private fun updatePlaybackState(state: Int, position: Long = 0L, duration: Long = 0L) {
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

    private fun updateNotification() {
        val notification = buildNotification()
        startForeground(Constants.Notification.NOTIFICATION_ID, notification)
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, Constants.Notification.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_launcher_background
                )
            )
            .setContentTitle(getCurrentSong()?.title)
            .setContentText(getCurrentSong()?.artistsNames)
            .setStyle(
                MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2) // Previous, play/pause, next
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()
    }

    private fun handlePlay(song: Song) {
        mediaPlayer.play(song.data)
        startPositionUpdates()
        updateNotification()
        sendMetadataChangedBroadcast(song)
    }

    private fun handleResume() {
        mediaPlayer.resume()
        startPositionUpdates()
        updateNotification()
    }

    private fun handlePause() {
        mediaPlayer.pause()
        updatePlaybackState(
            PlaybackStateCompat.STATE_PAUSED,
            mediaPlayer.getCurrentPosition(),
            mediaPlayer.getDuration()
        )
        stopPositionUpdates()
        updateNotification()
    }

    private fun handleStop() {
        mediaPlayer.stop()
        stopPositionUpdates()
        stopSelf()
    }

    private fun handlePrevious() {
        if (playlist.isNotEmpty() && currentSongIndex > 0) {
            currentSongIndex--
            handlePlay(playlist[currentSongIndex])
        } else {
            mediaPlayer.seekTo(0) // Restart current song if at start
        }
        updateNotification()
    }

    private fun handleNext() {
        if (playlist.isNotEmpty() && currentSongIndex < playlist.size - 1) {
            currentSongIndex++
            handlePlay(playlist[currentSongIndex])
        } else {
            mediaPlayer.stop() // Stop if no next song
            updatePlaybackState(PlaybackStateCompat.STATE_STOPPED)
            stopPositionUpdates()
        }
        updateNotification()
    }

    private fun handleSeekTo(position: Long) {
        mediaPlayer.seekTo(position)
        updatePlaybackState(
            if (mediaPlayer.isPlaying()) PlaybackStateCompat.STATE_PLAYING
            else PlaybackStateCompat.STATE_PAUSED,
            position,
            mediaPlayer.getDuration().toLong()
        )
        updateNotification()
    }

    private fun startPositionUpdates() {
        stopPositionUpdates()
        positionUpdateJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive && mediaPlayer.isPlaying()) {
                val position = mediaPlayer.getCurrentPosition().toLong()
                val duration = mediaPlayer.getDuration().toLong()
                updatePlaybackState(PlaybackStateCompat.STATE_PLAYING, position, duration)
                updateNotification() // Update notification dynamically
                delay(1000)
            }
        }
    }

    private fun stopPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = null
    }

    override fun onDestroy() {
        stopPositionUpdates()
        mediaPlayer.release()
        mediaSession.release()
        super.onDestroy()
    }

    private fun getCurrentSong() =
        if (currentSongIndex >= 0 && currentSongIndex < playlist.size) playlist[currentSongIndex]
        else null

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        fun startService(
            context: Context,
            action: String,
            playlist: List<Song>? = null,
            startIndex: Int = 0,
            position: Long? = null
        ) {
            val intent = Intent(context, MediaPlayerService::class.java).apply {
                this.action = action
                playlist?.let { putParcelableArrayListExtra(SONGS, ArrayList(it)) }
                putExtra(START_INDEX, startIndex)
                position?.let { putExtra(POSITION, it) }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}
