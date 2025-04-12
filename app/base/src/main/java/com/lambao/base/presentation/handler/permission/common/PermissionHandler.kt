package com.lambao.base.presentation.handler.permission.common

interface PermissionHandler {
    fun requestPermissions(permissions: List<String>, onResult: (Map<String, Boolean>) -> Unit)
    fun checkPermission(permission: String): Boolean
    fun shouldShowRationale(permission: String): Boolean
}