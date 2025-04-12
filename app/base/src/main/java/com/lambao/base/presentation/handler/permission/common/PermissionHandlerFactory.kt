package com.lambao.base.presentation.handler.permission.common

import androidx.fragment.app.FragmentActivity
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.presentation.handler.permission.CameraPermissionHandler
import com.lambao.base.presentation.handler.permission.NotificationPermissionHandler
import com.lambao.base.presentation.handler.permission.StoragePermissionHandler

object PermissionHandlerFactory {
    fun getHandler(
        permission: String,
        activity: FragmentActivity,
        dialogHandler: DialogHandler
    ): SpecificPermissionHandler = when (permission) {
        android.Manifest.permission.CAMERA -> CameraPermissionHandler(activity, dialogHandler)
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE -> StoragePermissionHandler(
            activity,
            dialogHandler
        )

        android.Manifest.permission.POST_NOTIFICATIONS -> NotificationPermissionHandler(
            activity,
            dialogHandler
        )

        else -> throw IllegalArgumentException("Unsupported permission: $permission")
    }
}