package com.lambao.base.presentation.handler.permission

interface PermissionHandler {
    fun getPermissions(): List<String>
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

    fun checkPermissionGranted(permission: String): Boolean
    fun isPermissionsGranted(): Boolean
    fun promptOpenSettings()
    fun onPermissionResult(result: PermissionResult)
}