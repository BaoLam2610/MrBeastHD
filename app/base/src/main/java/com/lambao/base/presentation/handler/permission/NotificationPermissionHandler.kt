package com.lambao.base.presentation.handler.permission

import android.Manifest
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.presentation.handler.permission.common.BasePermissionHandler
import com.lambao.base.presentation.handler.permission.common.SpecificPermissionHandler

class NotificationPermissionHandler(
    activity: FragmentActivity,
    dialogHandler: DialogHandler,
) : BasePermissionHandler(activity, dialogHandler), SpecificPermissionHandler {
    override val permissions: Array<String>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            emptyArray()
        }

    override val permissionDescription: String
        get() = "Please grant notification permission to use this feature"
}