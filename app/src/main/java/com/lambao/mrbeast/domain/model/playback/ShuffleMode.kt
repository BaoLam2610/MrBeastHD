package com.lambao.mrbeast.domain.model.playback

import androidx.annotation.DrawableRes
import com.lambao.mrbeast_music.R

enum class ShuffleMode(
    val key: Int,
    @DrawableRes val iconId: Int
) {
    OFF(0, R.drawable.ic_shuffle_disable),
    ON(1, R.drawable.ic_shuffle_enable);

    companion object {
        fun fromKey(key: Int): ShuffleMode {
            return ShuffleMode.entries.find { it.key == key } ?: OFF
        }
    }
}