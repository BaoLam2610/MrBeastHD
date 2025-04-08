package com.lambao.mrbeast.presentation.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.presentation.binding.ImageViewBindingAdapters.loadImageBitmap
import com.lambao.mrbeast.presentation.binding.ImageViewBindingAdapters.loadImageUrl
import com.lambao.mrbeast_music.R

object SongBindingAdapters {
    @JvmStatic
    @BindingAdapter(
        "songThumbnail",
        "placeholderResId",
        "errorResId",
        "isCircleCrop",
        "cornerRadius",
        requireAll = false
    )
    fun ImageView.setSongThumbnail(
        song: Song?, placeholderResId: Int? = null,
        errorResId: Int? = null,
        isCircleCrop: Boolean = false,
        cornerRadius: Int = 0
    ) {
        if (song == null) return
        val defaultPlaceholderResId = R.drawable.img_music_placeholder
        if (song.isOnline) {
            loadImageUrl(
                song.thumbnail,
                placeholderResId = placeholderResId ?: defaultPlaceholderResId,
                errorResId = errorResId ?: defaultPlaceholderResId,
                isCircleCrop = isCircleCrop,
                cornerRadius = cornerRadius
            )
        } else {
            loadImageBitmap(
                song.thumbnailBitmap,
                placeholderResId = placeholderResId ?: defaultPlaceholderResId,
                errorResId = errorResId ?: defaultPlaceholderResId,
                isCircleCrop = isCircleCrop,
                cornerRadius = cornerRadius
            )
        }
    }
}