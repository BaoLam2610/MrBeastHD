package com.lambao.base.presentation.handler.camera

import android.net.Uri

interface CameraHandler {
    val uri: Uri
    fun openCamera(onResult: (CameraResult) -> Unit)
    fun onResult(result: CameraResult)
}