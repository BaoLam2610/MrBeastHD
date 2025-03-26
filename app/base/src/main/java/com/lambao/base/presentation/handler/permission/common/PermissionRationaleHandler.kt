package com.lambao.base.presentation.handler.permission.common

interface PermissionRationaleHandler {
    fun showRationale(permissionDescription: String, onDismiss: () -> Unit)
}