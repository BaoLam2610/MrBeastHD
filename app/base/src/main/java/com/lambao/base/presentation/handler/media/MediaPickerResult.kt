package com.lambao.base.presentation.handler.media

import android.net.Uri

sealed class MediaPickerResult {
    data class Success(val uris: List<Uri>) : MediaPickerResult()
    data class Error(val message: String) : MediaPickerResult()
}