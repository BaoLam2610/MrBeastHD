package com.lambao.base.presentation.handler.permission

sealed class PermissionResult {
    data class Granted(val permissions: List<String>) : PermissionResult()
    data class Denied(
        val permissions: List<String>,
        val isPermanentlyDenied: Boolean
    ) : PermissionResult()
}