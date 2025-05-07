package com.lambao.base.presentation.handler.media

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.fragment.app.FragmentActivity
import com.lambao.base.R
import com.lambao.base.presentation.handler.dialog.DialogHandler

class MediaPickerHandlerImpl(
    private val mediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    private val dialogHandler: DialogHandler,
    private val activity: FragmentActivity
) : MediaPickerHandler {

    private var onMediaResult: ((MediaPickerResult) -> Unit)? = null

    override fun pickMedia(
        mediaType: PickVisualMediaRequest.Builder.() -> Unit,
        onResult: ((MediaPickerResult) -> Unit)?
    ) {
        try {
            val request = PickVisualMediaRequest.Builder()
                .apply(mediaType).build()
            onMediaResult = onResult
            mediaLauncher.launch(request)
        } catch (e: Exception) {
            dialogHandler.showAlertDialog(
                message = activity.getString(R.string.failed_to_open_media_picker),
            )
            onResult?.invoke(MediaPickerResult.Error(e.message ?: ""))
        }
    }

    override fun onResult(result: MediaPickerResult) {
        onMediaResult?.invoke(result)
        onMediaResult = null
    }
}