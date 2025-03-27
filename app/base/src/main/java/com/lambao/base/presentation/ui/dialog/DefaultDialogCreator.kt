package com.lambao.base.presentation.ui.dialog

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AlertDialog

class DefaultDialogCreator : DialogCreator {
    override fun createDialog(
        context: Context,
        title: String?,
        message: String,
        positiveText: String?,
        negativeText: String?,
        cancelable: Boolean,
        onPositiveClick: (() -> Unit)?,
        onNegativeClick: (() -> Unit)?,
        onDismiss: (() -> Unit)?
    ): Dialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(cancelable)
            .apply {
                if (positiveText != null) {
                    setPositiveButton(positiveText) { dialog, _ ->
                        onPositiveClick?.invoke()
                        dialog.dismiss()
                    }
                }
                if (negativeText != null) {
                    setNegativeButton(negativeText) { dialog, _ ->
                        onNegativeClick?.invoke()
                        dialog.dismiss()
                    }
                }
                setOnDismissListener { onDismiss?.invoke() }
            }
            .create()
    }
}