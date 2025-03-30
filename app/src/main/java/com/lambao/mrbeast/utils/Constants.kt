package com.lambao.mrbeast.utils

object Constants {

    const val BASE_URL = "https://m.vuiz.net/"
    const val URL_VER = "getlink/mp3zing/api.php"

    const val NETWORK_TIME_OUT = 3000L
    const val HTTP_CONTENT_TYPE_KEY = "Content-Type"
    const val HTTP_CONTENT_TYPE_VALUE = "application/json"
    const val AUTHORIZATION = "Authorization"

    const val PREF_FILE_NAME = "mrbeast_music_pref"

    object Argument {
        const val SONG = "SONG"
        const val SONGS = "SONGS"
        const val POSITION = "POSITION"
        const val THUMBNAILS = "THUMBNAILS"
    }

    object Notification {
        const val CHANNEL_ID = "music_channel"
        const val CHANNEL_NAME = "Music Channel"
        const val NOTIFICATION_ID = 1
    }

    object MediaAction {
        const val PLAY = "PLAY"
        const val PAUSE = "PAUSE"
        const val RESUME = "RESUME"
        const val STOP = "STOP"
        const val NEXT = "NEXT"
        const val PREVIOUS = "PREVIOUS"
        const val SEEK_TO = "SEEK_TO"
    }
}