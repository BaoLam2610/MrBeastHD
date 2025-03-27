package com.lambao.base.presentation.ui.dialog

import android.app.Dialog
import android.content.Context

interface DialogCreator {
    fun createDialog(
        context: Context,
        title: String?,
        message: String,
        positiveText: String?,
        negativeText: String?,
        cancelable: Boolean,
        onPositiveClick: (() -> Unit)?,
        onNegativeClick: (() -> Unit)?,
        onDismiss: (() -> Unit)?
    ): Dialog
}