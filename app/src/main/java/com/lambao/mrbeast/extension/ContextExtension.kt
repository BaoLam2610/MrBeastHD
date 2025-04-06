package com.lambao.mrbeast.extension

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide

fun Context.getBitmapFromUrl(url: String?): Bitmap? {
    if (url.isNullOrEmpty()) return null
    return try {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .submit()
            .get()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}