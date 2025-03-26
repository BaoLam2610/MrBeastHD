package com.lambao.base.presentation.handler.permission

import android.Manifest
import androidx.fragment.app.FragmentActivity
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.presentation.handler.permission.common.BasePermissionHandler
import com.lambao.base.presentation.handler.permission.common.DefaultPermissionRationaleHandler
import com.lambao.base.presentation.handler.permission.common.PermissionRationaleHandler
import com.lambao.base.presentation.handler.permission.common.SpecificPermissionHandler

class CameraPermissionHandler(
    activity: FragmentActivity,
    dialogHandler: DialogHandler,
    rationaleHandler: PermissionRationaleHandler = DefaultPermissionRationaleHandler(dialogHandler)
) : BasePermissionHandler(activity, dialogHandler, rationaleHandler), SpecificPermissionHandler {
    override val permissions: Array<String>
        get() = arrayOf(Manifest.permission.CAMERA)
}