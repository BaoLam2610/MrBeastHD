package com.lambao.mrbeast.extension

import android.content.res.Resources.getSystem

val Int.toPx: Int get() = (this / getSystem().displayMetrics.density).toInt()

val Int.toDp: Int get() = (this * getSystem().displayMetrics.density).toInt()