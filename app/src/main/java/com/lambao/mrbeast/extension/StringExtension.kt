package com.lambao.mrbeast.extension

import java.util.Locale

fun Long.toTimeString(): String {
    val seconds = this
    return when {
        seconds < 3600 -> {
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            String.format(Locale.US, "%02d:%02d", minutes, remainingSeconds)
        }

        else -> {
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60
            String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, remainingSeconds)
        }
    }
}

//fun Long.toTimeString(): String {
//    val seconds = (this / 1000) % 60
//    val minutes = (this / (1000 * 60)) % 60
//    return String.format("%02d:%02d", minutes, seconds)
//}