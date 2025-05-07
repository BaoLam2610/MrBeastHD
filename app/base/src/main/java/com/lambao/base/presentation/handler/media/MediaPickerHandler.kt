package com.lambao.base.presentation.handler.media

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

interface MediaPickerHandler {
    fun pickMedia(
        mediaType: PickVisualMediaRequest.Builder.() -> Unit = {
            setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
        },
        maxItems: Int = 1,
        onResult: ((MediaPickerResult) -> Unit)? = null
    )

    fun onResult(result: MediaPickerResult)
}