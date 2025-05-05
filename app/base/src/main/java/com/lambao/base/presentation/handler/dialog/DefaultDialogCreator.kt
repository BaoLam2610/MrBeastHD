package com.lambao.base.presentation.handler.dialog

import android.content.Context
import androidx.fragment.app.DialogFragment
import com.lambao.base.presentation.ui.view.message.MessageDialog

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
    ): DialogFragment {
        return MessageDialog.newInstance(
            title,
            message,
            positiveText,
            negativeText,
            cancelable,
            onPositiveClick,
            onNegativeClick,
            onDismiss
        )
    }
}