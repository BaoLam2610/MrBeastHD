package com.lambao.mrbeast.domain.model.playback

import androidx.annotation.DrawableRes
import com.lambao.mrbeast_music.R

enum class RepeatMode(
    val key: Int,
    @DrawableRes val iconId: Int
) {
    NONE(0, R.drawable.ic_repeat_disable),
    ALL(1, R.drawable.ic_repeat),
    ONE(2, R.drawable.ic_repeat_once);

    companion object {
        fun fromKey(key: Int): RepeatMode {
            return RepeatMode.entries.find { it.key == key } ?: NONE
        }
    }
}