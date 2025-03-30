package com.lambao.mrbeast.presentation.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

object ImageViewBindingAdapters {
    /**
     * Loads an image from a URL into an ImageView using Glide with customizable options.
     *
     * @param url The URL of the image to load.
     * @param placeholderResId Resource ID of the placeholder image (optional).
     * @param errorResId Resource ID of the error image (optional).
     * @param isCircleCrop Whether to apply a circle crop transformation (default: false).
     * @param cornerRadius Radius in pixels for rounded corners (default: 0, no rounding).
     */
    @BindingAdapter(
        "imageUrl",
        "placeholderResId",
        "errorResId",
        "isCircleCrop",
        "cornerRadius",
        requireAll = false
    )
    fun ImageView.loadImage(
        url: String?,
        placeholderResId: Int? = null,
        errorResId: Int? = null,
        isCircleCrop: Boolean = false,
        cornerRadius: Int = 0
    ) {
        if (url.isNullOrBlank()) {
            placeholderResId?.let { setImageResource(it) }
            return
        }

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .let { options ->
                placeholderResId?.let { resId -> options.placeholder(resId) } ?: options
            }
            .let { options ->
                errorResId?.let { resId -> options.error(resId) } ?: options
            }
            .let { options ->
                when {
                    isCircleCrop -> options.circleCrop()
                    cornerRadius > 0 -> options.transform(
                        CenterCrop(),
                        RoundedCorners(cornerRadius)
                    )

                    else -> options
                }
            }

        Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .into(this)
    }

    @BindingAdapter("imageRes")
    fun ImageView.imageRes(res: Int) {
        setImageResource(res)
    }
}