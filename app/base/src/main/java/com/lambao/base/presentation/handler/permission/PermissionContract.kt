package com.lambao.base.presentation.handler.permission

interface PermissionContract {
    fun requestPermission(
        permission: String,
        settingsMessage: String? = null,
        onResult: ((PermissionResult) -> Unit)? = null
    )

    fun requestPermissions(
        permissions: List<String>,
        settingsMessage: String? = null,
        onResult: ((PermissionResult) -> Unit)? = null
    )

    fun checkPermission(permission: String): Boolean
}