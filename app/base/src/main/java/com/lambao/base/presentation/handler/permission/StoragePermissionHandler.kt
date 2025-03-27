package com.lambao.base.presentation.handler.permission

import android.Manifest
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.lambao.base.R
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.presentation.handler.permission.common.BasePermissionHandler
import com.lambao.base.presentation.handler.permission.common.SpecificPermissionHandler

class StoragePermissionHandler(
    activity: FragmentActivity,
    dialogHandler: DialogHandler,
) : BasePermissionHandler(activity, dialogHandler), SpecificPermissionHandler {
    override val permissions: Array<String>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    override val permissionDescription: String
        get() = activity.getString(R.string.storage_permission_description)
}