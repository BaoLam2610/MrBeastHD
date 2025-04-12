package com.lambao.base.presentation.handler.permission

import androidx.fragment.app.FragmentActivity
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.presentation.handler.permission.common.BasePermissionHandler
import com.lambao.base.presentation.handler.permission.common.SpecificPermissionHandler

class DynamicPermissionHandler(
    activity: FragmentActivity,
    dialogHandler: DialogHandler,
    override val permissions: List<String>,
    override val permissionDescription: String,
) : BasePermissionHandler(activity, dialogHandler), SpecificPermissionHandler