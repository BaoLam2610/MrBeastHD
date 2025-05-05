package com.lambao.base.presentation.handler.dialog

import android.content.Context
import androidx.fragment.app.DialogFragment

interface DialogCreator {
    fun createDialog(
        context: Context,
        title: String?,
        message: String,
        positiveText: String?,
        negativeText: String?,
        cancelable: Boolean = true,
        onPositiveClick: (() -> Unit)?,
        onNegativeClick: (() -> Unit)?,
        onDismiss: (() -> Unit)?
    ): DialogFragment
}