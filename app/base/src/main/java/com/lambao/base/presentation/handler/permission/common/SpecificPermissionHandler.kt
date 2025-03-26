package com.lambao.base.presentation.handler.permission.common

interface SpecificPermissionHandler : PermissionHandler {
    val permissions: Array<String>
    fun request(onResult: (Map<String, Boolean>) -> Unit) {
        requestPermissions(permissions, onResult)
    }
}