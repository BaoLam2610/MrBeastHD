package com.lambao.mrbeast.utils

object Constants {

    const val BASE_URL = "https://m.vuiz.net/"
    const val URL_VER = "getlink/mp3zing/api.php"

    const val ZINGMP3_DOMAIN = "https://zingmp3.vn"

    const val NETWORK_TIME_OUT = 3000L
    const val HTTP_CONTENT_TYPE_KEY = "Content-Type"
    const val HTTP_CONTENT_TYPE_VALUE = "application/json"
    const val AUTHORIZATION = "Authorization"

    const val PREF_FILE_NAME = "mrbeast_music_pref"

    object Argument {
        const val SONG = "SONG"
        const val PLAYLIST = "PLAYLIST"
        const val POSITION = "POSITION"
        const val START_INDEX = "START_INDEX"
        const val THUMBNAILS = "THUMBNAILS"
        const val STATE = "STATE"
        const val DURATION = "DURATION"
        const val REPEAT_MODE = "REPEAT_MODE"
        const val SHUFFLE_MODE = "SHUFFLE_MODE"
    }

    object Notification {
        const val CHANNEL_ID = "music_channel"
        const val CHANNEL_NAME = "Music Channel"
        const val NOTIFICATION_ID = 1
    }

    object Media {
        const val DEFAULT_TIME = "00:00"
    }

    object Broadcast {
        const val ACTION_PLAYBACK_STATE_CHANGED = "ACTION_PLAYBACK_STATE_CHANGED"
        const val ACTION_METADATA_CHANGED = "ACTION_METADATA_CHANGED"
    }

    object Preference {
        const val REPEAT = "REPEAT"
        const val SHUFFLE = "SHUFFLE"
    }
}