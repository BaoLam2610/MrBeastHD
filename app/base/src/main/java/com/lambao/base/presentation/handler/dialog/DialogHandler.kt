package com.lambao.base.presentation.handler.dialog

interface DialogHandler {
    fun showAlertDialog(message: String)
    fun showAlertDialog(
        title: String?,
        message: String,
        positiveText: String?,
        negativeText: String?,
        cancelable: Boolean = true,
        shouldShowPositiveButton: Boolean = true,
        shouldShowNegativeButton: Boolean = true,
        onPositiveListener: (() -> Unit)? = null,
        onNegativeListener: (() -> Unit)? = null,
        onDismissListener: (() -> Unit)? = null,
    )
}