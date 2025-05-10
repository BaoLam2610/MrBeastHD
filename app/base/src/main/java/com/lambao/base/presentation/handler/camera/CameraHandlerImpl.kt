package com.lambao.base.presentation.handler.camera

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.lambao.base.R
import com.lambao.base.presentation.handler.dialog.DialogHandler
import java.io.File

class CameraHandlerImpl(
    private val cameraLauncher: ActivityResultLauncher<Uri>,
    private val dialogHandler: DialogHandler,
    private val activity: FragmentActivity
) : CameraHandler {
    private var onCameraResult: ((CameraResult) -> Unit)? = null

    private val _uri by lazy {
        val imageFile = File(activity.filesDir, "${System.currentTimeMillis()}.png")
        FileProvider.getUriForFile(
            activity,
            "${activity.packageName}.FileProvider",
            imageFile
        )
    }

    override val uri: Uri get() = _uri

    override fun openCamera(onResult: (CameraResult) -> Unit) {
        try {
            onCameraResult = onResult
            cameraLauncher.launch(_uri)
        } catch (e: Exception) {
            e.printStackTrace()
            dialogHandler.showAlertDialog(activity.getString(R.string.failed_to_open_camera) + ". " + e.message)
        }
    }

    override fun onResult(result: CameraResult) {
        onCameraResult?.invoke(result)
        onCameraResult = null
    }
}