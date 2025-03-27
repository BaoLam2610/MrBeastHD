package com.lambao.base.presentation.handler.dialog

import android.app.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.lambao.base.presentation.ui.dialog.DefaultDialogCreator
import com.lambao.base.presentation.ui.dialog.DialogCreator
import com.lambao.base.utils.log

class DialogHandlerImpl(
    private val activity: FragmentActivity,
    private val dialogCreator: DialogCreator = DefaultDialogCreator()
) : DialogHandler {

    private var currentDialog: Dialog? = null

    override fun showRationaleDialog(
        title: String,
        permissionDescription: String,
        positiveText: String,
        negativeText: String,
        onPositiveListener: (() -> Unit)?,
        onNegativeListener: (() -> Unit)?
    ) {
        showAlertDialog(
            title = title,
            message = permissionDescription,
            positiveText = positiveText,
            negativeText = negativeText,
            cancelable = false,
            shouldShowPositiveButton = true,
            shouldShowNegativeButton = true,
            onPositiveListener = onPositiveListener,
            onNegativeListener = onNegativeListener
        )
    }

    override fun showAlertDialog(message: String) {
        showAlertDialog(
            title = null,
            message = message,
            positiveText = "OK",
            negativeText = null,
            cancelable = false,
            shouldShowPositiveButton = true,
            shouldShowNegativeButton = false
        )
    }

    override fun showAlertDialog(
        title: String?,
        message: String,
        positiveText: String?,
        negativeText: String?,
        cancelable: Boolean,
        shouldShowPositiveButton: Boolean,
        shouldShowNegativeButton: Boolean,
        onPositiveListener: (() -> Unit)?,
        onNegativeListener: (() -> Unit)?,
        onDismissListener: (() -> Unit)?
    ) {
        if (!activity.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            log("Cannot show dialog: Activity is not in STARTED state")
            return
        }

        currentDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }

        val newDialog = dialogCreator.createDialog(
            context = activity,
            title = title,
            message = message,
            positiveText = if (shouldShowPositiveButton) positiveText else null,
            negativeText = if (shouldShowNegativeButton) negativeText else null,
            cancelable = cancelable,
            onPositiveClick = onPositiveListener,
            onNegativeClick = onNegativeListener,
            onDismiss = {
                onDismissListener?.invoke()
                currentDialog = null
            }
        )

        currentDialog = newDialog
        newDialog.show()
    }
}