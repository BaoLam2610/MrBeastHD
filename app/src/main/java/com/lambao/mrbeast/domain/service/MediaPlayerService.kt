package com.lambao.mrbeast.domain.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.lambao.mrbeast.domain.model.MediaAction
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.service.media_player.BaseMediaPlayer
import com.lambao.mrbeast.domain.service.media_player.MediaPlayerCallBack
import com.lambao.mrbeast.domain.service.media_player.MediaPlayerImpl
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast_music.R

class MediaPlayerService : Service() {
    private val binder = MediaPlayerBinder()
    private lateinit var mediaPlayer: BaseMediaPlayer
    private var song: Song? = null

    inner class MediaPlayerBinder : Binder() {
        fun getService(): MediaPlayerService = this@MediaPlayerService
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayerImpl()
        mediaPlayer.setCallBack(object : MediaPlayerCallBack {
            override fun onStart() {
                sendNotification(song) // Cần có dữ liệu bài hát
            }

            override fun onComplete() {
                sendNotification(song, isPlaying = false)
            }

            override fun onError(e: Exception) {
                sendNotification(song, isPlaying = false)
            }

            override fun onPositionChanged(position: Int, duration: Int) {
                // Có thể cập nhật notification với tiến độ nếu cần
            }
        })
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            MediaAction.PLAY_PAUSE.name -> {
                if (mediaPlayer.isPlaying()) mediaPlayer.pause() else mediaPlayer.play(song?.data) // Cần URL thực tế
                sendNotification(song, mediaPlayer.isPlaying())
            }

            MediaAction.PREVIOUS.name -> {
                // Logic cho bài trước (nếu có danh sách phát)
            }

            MediaAction.NEXT.name -> {
                // Logic cho bài tiếp theo (nếu có danh sách phát)
            }

            MediaAction.CLOSE.name -> {
                mediaPlayer.stop()
                stopForeground(true)
                stopSelf()
            }
        }
        return START_STICKY
    }

    // Hàm gửi notification
    private fun sendNotification(song: Song?, isPlaying: Boolean = mediaPlayer.isPlaying()) {
        if (song == null) return
        val session = MediaSessionCompat(this, "music")
        val style = MediaStyle()
            .setShowActionsInCompactView(0, 1, 2, 3)
//            .setMediaSession(session.sessionToken) // show seekbar on notification system

        val notification = NotificationCompat.Builder(this, Constants.Notification.CHANNEL_ID)
            .setStyle(style)
            .setContentTitle(song.title)
            .setContentText(song.artistsNames)
            .addAction(
                R.drawable.ic_previous_24,
                MediaAction.PREVIOUS.name,
                createPendingIntent(MediaAction.PREVIOUS)
            )
            .addAction(
                if (isPlaying) R.drawable.ic_pause_24 else R.drawable.ic_play_24,
                MediaAction.PLAY_PAUSE.name,
                createPendingIntent(MediaAction.PLAY_PAUSE)
            )
            .addAction(
                R.drawable.ic_next_24,
                MediaAction.NEXT.name,
                createPendingIntent(MediaAction.NEXT)
            )
            .addAction(
                R.drawable.ic_close_24,
                MediaAction.CLOSE.name,
                createPendingIntent(MediaAction.CLOSE)
            )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_launcher_background
                )
            )
            .setOngoing(isPlaying) // Đảm bảo notification không bị xóa khi đang phát
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startForeground(1, notification)
            }
        } else {
            startForeground(1, notification)
        }
    }

    private fun createPendingIntent(mediaAction: MediaAction): PendingIntent {
        val intent = Intent(this, MediaPlayerService::class.java).apply {
            action = mediaAction.name
        }
        return PendingIntent.getService(
            this,
            mediaAction.requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        stopForeground(true)
    }

    // Các phương thức public để điều khiển từ binder
    fun playSong(song: Song?) {
        this.song = song
        mediaPlayer.play(song?.data)
        sendNotification(song, isPlaying = true)
    }
    fun play(data: String?) = mediaPlayer.play(data)
    fun pause() = mediaPlayer.pause()
    fun stop() = mediaPlayer.stop()
    fun seekTo(position: Int) = mediaPlayer.seekTo(position)
    fun isPlaying(): Boolean = mediaPlayer.isPlaying()
    fun setCallBack(callBack: MediaPlayerCallBack) = mediaPlayer.setCallBack(callBack)
}