package com.lambao.base.extension

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.getAppName(): String {
    var applicationName = ""
    try {
        val applicationInfo = applicationInfo
        val stringId = applicationInfo.labelRes
        applicationName = if (stringId == 0) applicationInfo.nonLocalizedLabel.toString()
        else getString(stringId)

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return applicationName
}


/**
 * Checks if the app has storage permissions (READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE).
 *
 * @return True if permissions are granted, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val hasPermission = context.hasStoragePermission()
 * ```
 */
fun Context.hasStoragePermission(): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // No permissions needed for MediaStore on API 29+
    }
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
