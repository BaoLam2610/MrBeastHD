package com.lambao.mrbeast.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lambao.mrbeast_music.R

enum class MenuItem(
    @StringRes val titleId: Int,
    @DrawableRes val icon: Int
) {
    DISCOVER(R.string.discover, R.drawable.ic_light_on),
    MY_MUSIC(R.string.my_music, R.drawable.ic_user),
    FAVORITE_SONG(R.string.favorite_song, R.drawable.ic_heart),
    LANGUAGE(R.string.language, R.drawable.ic_global)
}