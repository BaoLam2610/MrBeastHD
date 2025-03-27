package com.lambao.base.presentation.handler.permission

import android.Manifest
import androidx.fragment.app.FragmentActivity
import com.lambao.base.R
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.presentation.handler.permission.common.BasePermissionHandler
import com.lambao.base.presentation.handler.permission.common.SpecificPermissionHandler

class CameraPermissionHandler(
    activity: FragmentActivity,
    dialogHandler: DialogHandler,
) : BasePermissionHandler(activity, dialogHandler), SpecificPermissionHandler {
    override val permissions: Array<String>
        get() = arrayOf(Manifest.permission.CAMERA)

    override val permissionDescription: String
        get() = activity.getString(R.string.camera_permission_description)
}