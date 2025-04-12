package com.lambao.base.presentation.handler.permission.common

interface SpecificPermissionHandler : PermissionHandler {
    val permissions: List<String>
    fun request(onResult: (Map<String, Boolean>) -> Unit) {
        requestPermissions(permissions, onResult)
    }
}