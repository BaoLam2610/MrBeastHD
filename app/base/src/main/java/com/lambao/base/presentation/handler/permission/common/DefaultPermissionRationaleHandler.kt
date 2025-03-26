package com.lambao.base.presentation.handler.permission.common

import com.lambao.base.presentation.handler.dialog.DialogHandler

class DefaultPermissionRationaleHandler(
    private val dialogHandler: DialogHandler
) : PermissionRationaleHandler {
    override fun showRationale(permissionDescription: String, onDismiss: () -> Unit) {
        dialogHandler.showMessageDialog(permissionDescription)
    }
}