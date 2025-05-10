package com.lambao.base.presentation.handler.media

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia

class CustomPickMultipleVisualMedia : ActivityResultContracts.PickMultipleVisualMedia() {
    private var maxItems: Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) MediaStore.getPickImagesMaxLimit()
        else 100

    fun updateMaxItems(newMaxItems: Int) {
        maxItems = if (newMaxItems <= 1) 2 else newMaxItems
    }

    override fun createIntent(context: Context, input: PickVisualMediaRequest): Intent {
        val intent = super.createIntent(context, input)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, maxItems)
            }
            intent.putExtra(PickVisualMedia.EXTRA_SYSTEM_FALLBACK_PICK_IMAGES_MAX, maxItems)
        } catch (_: Exception) {
        }
        return intent
    }
}