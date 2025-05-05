package com.lambao.base.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.GrayscaleTransformation
import java.io.File

/**
 * Loads an image from a URL into the ImageView.
 *
 * @param url The URL of the image to load.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 * @param diskCacheStrategy Cache strategy for the image.
 */
fun ImageView.loadImage(
    url: String?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.ALL
) {
    Glide.with(this)
        .load(url)
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(diskCacheStrategy)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image from a resource ID into the ImageView.
 *
 * @param resourceId The resource ID of the image.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadImage(
    @DrawableRes resourceId: Int,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(resourceId)
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image from a Uri into the ImageView.
 *
 * @param uri The Uri of the image to load.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadImage(
    uri: Uri?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(uri)
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image from a Bitmap into the ImageView.
 *
 * @param bitmap The Bitmap to load.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadImage(
    bitmap: Bitmap?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(bitmap)
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image from a File into the ImageView.
 *
 * @param file The File containing the image.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadImage(
    file: File?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(file)
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.DATA)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image from a byte array into the ImageView.
 *
 * @param bytes The byte array containing the image data.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadImage(
    bytes: ByteArray?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(bytes)
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads a Drawable into the ImageView.
 *
 * @param drawable The Drawable to load.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadDrawable(
    drawable: Drawable?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(drawable)
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads a circular cropped image from a URL into the ImageView.
 *
 * @param url The URL of the image to load.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadCircleImage(
    url: String?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions().transform(CircleCrop()))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads a circular cropped image from a resource ID into the ImageView.
 *
 * @param resourceId The resource ID of the image.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadCircleImage(
    @DrawableRes resourceId: Int,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(resourceId)
        .apply(RequestOptions().transform(CircleCrop()))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads a circular cropped image from a Uri into the ImageView.
 *
 * @param uri The Uri of the image to load.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadCircleImage(
    uri: Uri?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(uri)
        .apply(RequestOptions().transform(CircleCrop()))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads a circular cropped image from a Bitmap into the ImageView.
 *
 * @param bitmap The Bitmap to load.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadCircleImage(
    bitmap: Bitmap?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(bitmap)
        .apply(RequestOptions().transform(CircleCrop()))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads a circular cropped image from a File into the ImageView.
 *
 * @param file The File containing the image.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadCircleImage(
    file: File?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(file)
        .apply(RequestOptions().transform(CircleCrop()))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.DATA)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads a circular cropped image from a byte array into the ImageView.
 *
 * @param bytes The byte array containing the image data.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadCircleImage(
    bytes: ByteArray?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(bytes)
        .apply(RequestOptions().transform(CircleCrop()))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads a circular cropped image from a Drawable into the ImageView.
 *
 * @param drawable The Drawable to load.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadCircleImage(
    drawable: Drawable?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(drawable)
        .apply(RequestOptions().transform(CircleCrop()))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image with rounded corners from a URL into the ImageView.
 *
 * @param url The URL of the image to load.
 * @param cornerRadiusDp The radius of the rounded corners in dp.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadRoundedImage(
    url: String?,
    cornerRadiusDp: Int,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    val cornerRadiusPx = cornerRadiusDp.toDp
    Glide.with(this)
        .load(url)
        .apply(RequestOptions().transform(RoundedCorners(cornerRadiusPx)))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image with rounded corners from a resource ID into the ImageView.
 *
 * @param resourceId The resource ID of the image.
 * @param cornerRadiusDp The radius of the rounded corners in dp.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadRoundedImage(
    @DrawableRes resourceId: Int,
    cornerRadiusDp: Int,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    val cornerRadiusPx = cornerRadiusDp.toDp
    Glide.with(this)
        .load(resourceId)
        .apply(RequestOptions().transform(RoundedCorners(cornerRadiusPx)))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image with rounded corners from a Uri into the ImageView.
 *
 * @param uri The Uri of the image to load.
 * @param cornerRadiusDp The radius of the rounded corners in dp.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadRoundedImage(
    uri: Uri?,
    cornerRadiusDp: Int,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    val cornerRadiusPx = cornerRadiusDp.toDp
    Glide.with(this)
        .load(uri)
        .apply(RequestOptions().transform(RoundedCorners(cornerRadiusPx)))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image with rounded corners from a Bitmap into the ImageView.
 *
 * @param bitmap The Bitmap to load.
 * @param cornerRadiusDp The radius of the rounded corners in dp.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadRoundedImage(
    bitmap: Bitmap?,
    cornerRadiusDp: Int,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    val cornerRadiusPx = cornerRadiusDp.toDp
    Glide.with(this)
        .load(bitmap)
        .apply(RequestOptions().transform(RoundedCorners(cornerRadiusPx)))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image with rounded corners from a File into the ImageView.
 *
 * @param file The File containing the image.
 * @param cornerRadiusDp The radius of the rounded corners in dp.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadRoundedImage(
    file: File?,
    cornerRadiusDp: Int,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    val cornerRadiusPx = cornerRadiusDp.toDp
    Glide.with(this)
        .load(file)
        .apply(RequestOptions().transform(RoundedCorners(cornerRadiusPx)))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.DATA)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image with rounded corners from a byte array into the ImageView.
 *
 * @param bytes The byte array containing the image data.
 * @param cornerRadiusDp The radius of the rounded corners in dp.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadRoundedImage(
    bytes: ByteArray?,
    cornerRadiusDp: Int,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    val cornerRadiusPx = cornerRadiusDp.toDp
    Glide.with(this)
        .load(bytes)
        .apply(RequestOptions().transform(RoundedCorners(cornerRadiusPx)))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image with rounded corners from a Drawable into the ImageView.
 *
 * @param drawable The Drawable to load.
 * @param cornerRadiusDp The radius of the rounded corners in dp.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadRoundedImage(
    drawable: Drawable?,
    cornerRadiusDp: Int,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    val cornerRadiusPx = cornerRadiusDp.toDp
    Glide.with(this)
        .load(drawable)
        .apply(RequestOptions().transform(RoundedCorners(cornerRadiusPx)))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads an image with custom size from a URL into the ImageView.
 *
 * @param url The URL of the image to load.
 * @param width The desired width of the image in pixels.
 * @param height The desired height of the image in pixels.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadImageWithSize(
    url: String?,
    width: Int,
    height: Int,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(url)
        .override(width, height)
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads a blurred image from a URL into the ImageView (requires Glide Transformations library).
 *
 * @param url The URL of the image to load.
 * @param blurRadius The blur radius (1-25, default 10 for performance).
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadBlurImage(
    url: String?,
    blurRadius: Int = 10,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.bitmapTransform(BlurTransformation(blurRadius)))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Loads a grayscale image from a URL into the ImageView.
 *
 * @param url The URL of the image to load.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadGrayscaleImage(
    url: String?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions().transform(GrayscaleTransformation()))
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

/**
 * Preloads an image from a URL to cache without displaying it.
 *
 * @param url The URL of the image to preload.
 */
fun Context.preloadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .preload()
}

/**
 * Loads an image and retrieves the Bitmap asynchronously.
 *
 * @param url The URL of the image to load.
 * @param onBitmapLoaded Callback invoked when the Bitmap is loaded.
 * @param onError Callback invoked if loading fails with the exception, if available.
 */
fun Context.loadBitmap(
    url: String?,
    onBitmapLoaded: (Bitmap) -> Unit,
    onError: (Exception?) -> Unit = {}
) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                onBitmapLoaded(resource)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                onError(GlideException("Bitmap load failed for URL: $url"))
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
}

/**
 * Loads an image with a listener for load success or failure.
 *
 * @param url The URL of the image to load.
 * @param onSuccess Callback invoked when the image loads successfully.
 * @param onError Callback invoked if loading fails.
 * @param placeholder Optional drawable to show while loading.
 * @param error Optional drawable to show if loading fails.
 */
fun ImageView.loadImageWithListener(
    url: String?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null,
    onSuccess: () -> Unit,
    onError: (Exception?) -> Unit = {}
) {
    Glide.with(this)
        .load(url)
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
            diskCacheStrategy(DiskCacheStrategy.ALL)
        }
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                onError(e)
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                onSuccess()
                return false
            }
        })
        .into(this)
}

/**
 * Skip memory cache for this image load.
 */
fun <T> RequestBuilder<T>.skipMemoryCache(): RequestBuilder<T> {
    return this.apply(RequestOptions().skipMemoryCache(true))
}

/**
 * Use a custom signature to control caching behavior.
 */
fun <T> RequestBuilder<T>.withSignature(signature: String): RequestBuilder<T> {
    return this.apply(RequestOptions().signature(ObjectKey(signature)))
}

/**
 * Preload images for faster display.
 */
fun Context.preloadImages(urls: List<String>) {
    urls.forEach { url ->
        if (url.isNotEmpty()) {
            Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .preload()
        }
    }
}

/**
 * Clears the current Glide request for the ImageView.
 */
fun ImageView.clearGlide() {
    Glide.with(this).clear(this)
}