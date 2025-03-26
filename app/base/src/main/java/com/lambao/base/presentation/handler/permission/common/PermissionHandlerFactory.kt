package com.lambao.base.presentation.handler.permission.common

import androidx.fragment.app.FragmentActivity
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.presentation.handler.permission.CameraPermissionHandler
import com.lambao.base.presentation.handler.permission.NotificationPermissionHandler
import com.lambao.base.presentation.handler.permission.StoragePermissionHandler

object PermissionHandlerFactory {
    fun getHandler(
        type: PermissionType,
        activity: FragmentActivity,
        dialogHandler: DialogHandler
    ): SpecificPermissionHandler =
        when (type) {
            PermissionType.STORAGE -> StoragePermissionHandler(activity, dialogHandler)
            PermissionType.NOTIFICATION -> NotificationPermissionHandler(activity, dialogHandler)
            PermissionType.CAMERA -> CameraPermissionHandler(activity, dialogHandler)
        }

    enum class PermissionType {
        STORAGE, NOTIFICATION, CAMERA
    }
}