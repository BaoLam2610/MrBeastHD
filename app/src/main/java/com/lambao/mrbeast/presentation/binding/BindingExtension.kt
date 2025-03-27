package com.lambao.mrbeast.presentation.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("imageRes")
fun ImageView.imageRes(res: Int) {
    setImageResource(res)
}

@BindingAdapter("textRes")
fun TextView.setTextRes(res: Int) {
    try {
        if (res != 0) {
            setText(res)
        }
    } catch (e: Exception) {
        text = ""
    }
}