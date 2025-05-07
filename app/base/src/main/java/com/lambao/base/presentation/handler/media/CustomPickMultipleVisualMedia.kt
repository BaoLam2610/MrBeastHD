package com.lambao.base.presentation.handler.media

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class CustomPickMultipleVisualMedia : ActivityResultContracts.PickMultipleVisualMedia() {
    private var maxItems: Int = 1

    fun updateMaxItems(newMaxItems: Int) {
        if (newMaxItems < 1) {
            maxItems = 1
            return
        }
        maxItems = newMaxItems
    }

    override fun createIntent(context: Context, input: PickVisualMediaRequest): Intent {
        val intent = super.createIntent(context, input)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, maxItems)
        }
        return intent
    }
}