package com.lambao.base.presentation.handler.camera

import android.net.Uri

sealed class CameraResult {
    data class Success(val uri: Uri) : CameraResult()
    data class Error(val message: String) : CameraResult()
}