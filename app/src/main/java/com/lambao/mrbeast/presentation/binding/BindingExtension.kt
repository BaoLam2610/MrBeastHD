package com.lambao.mrbeast.presentation.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.lambao.base.extension.click
import com.lambao.base.presentation.ui.view.OnSingleClickListener


@BindingAdapter("textRes")
fun TextView.setTextRes(res: Int) {
    try {
        if (res != 0) {
            setText(res)
        }
    } catch (_: Exception) {
        text = ""
    }
}

@BindingAdapter("click")
fun View.setOnSingleClick(func: View.OnClickListener?) {
    if (func == null) {
        setOnClickListener(null)
    } else {
        setOnClickListener(
            object : OnSingleClickListener() {
                override fun onSingleClick(v: View?) {
                    v?.let { func.onClick(it) }
                }
            }
        )
    }
}

@BindingAdapter("backgroundRes")
fun View.setBackgroundRes(@DrawableRes resId: Int?) {
    if (resId != null && resId != 0) {
        try {
            val drawable: Drawable? = try {
                ContextCompat.getDrawable(context, resId)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            background = drawable
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
        background = null
    }
}