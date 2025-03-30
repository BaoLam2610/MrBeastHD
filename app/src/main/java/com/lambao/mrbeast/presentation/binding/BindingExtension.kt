package com.lambao.mrbeast.presentation.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter


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