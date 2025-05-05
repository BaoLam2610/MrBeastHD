package com.lambao.mrbeast.domain.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.extension.getBitmapFromUrl
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast_music.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    init {
        createNotificationChannel()
    }

    fun buildNotification(
        sessionToken: MediaSessionCompat.Token,
        song: Song?,
        onBuilt: (Notification) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = if (song?.isOnline == true) {
                context.getBitmapFromUrl(song.thumbnail)
            } else {
                song?.thumbnailBitmap
            } ?: BitmapFactory.decodeResource(
                context.resources,
                R.drawable.img_music_placeholder
            )

            val notification =
                NotificationCompat.Builder(context, Constants.Notification.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setLargeIcon(bitmap)
                    .setContentTitle(song?.title)
                    .setContentText(song?.artistsNames)
                    .setStyle(
                        MediaStyle()
                            .setMediaSession(sessionToken)
                    )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setOngoing(true)
                    .build()

            withContext(Dispatchers.Main) {
                onBuilt(notification)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.Notification.CHANNEL_ID,
                Constants.Notification.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}
