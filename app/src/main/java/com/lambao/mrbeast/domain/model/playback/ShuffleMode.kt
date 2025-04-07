package com.lambao.mrbeast.domain.model.playback

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.lambao.mrbeast_music.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ShuffleMode(
    val key: Int,
    @DrawableRes val iconId: Int
) : Parcelable {
    OFF(0, R.drawable.ic_shuffle_disable),
    ON(1, R.drawable.ic_shuffle_enable);

    companion object {
        fun fromKey(key: Int): ShuffleMode {
            return ShuffleMode.entries.find { it.key == key } ?: OFF
        }
    }
}