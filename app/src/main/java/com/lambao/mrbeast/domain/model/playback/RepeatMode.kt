package com.lambao.mrbeast.domain.model.playback

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.lambao.mrbeast_music.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RepeatMode(
    val key: Int,
    @DrawableRes val iconId: Int
) : Parcelable {
    NONE(0, R.drawable.ic_repeat_disable),
    ALL(1, R.drawable.ic_repeat),
    ONE(2, R.drawable.ic_repeat_once);

    companion object {
        fun fromKey(key: Int): RepeatMode {
            return RepeatMode.entries.find { it.key == key } ?: NONE
        }
    }
}