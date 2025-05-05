package com.lambao.base.presentation.handler.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.lambao.base.R
import com.lambao.base.utils.log

class DialogHandlerImpl(
    private val activity: FragmentActivity,
    private val dialogCreator: DialogCreator = DefaultDialogCreator()
) : DialogHandler {

    private var currentDialog: DialogFragment? = null

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
            onPositiveListener = onPositiveListener,
            onNegativeListener = onNegativeListener
        )
    }

    override fun showAlertDialog(message: String) {
        showAlertDialog(
            title = null,
            message = message,
            positiveText = activity.getString(R.string.cancel),
            negativeText = null,
            cancelable = false
        )
    }

    override fun showAlertDialog(
        title: String?,
        message: String,
        positiveText: String?,
        negativeText: String?,
        cancelable: Boolean,
        onPositiveListener: (() -> Unit)?,
        onNegativeListener: (() -> Unit)?,
        onDismissListener: (() -> Unit)?
    ) {
        if (!activity.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            log("Cannot show dialog: Activity is not in STARTED state")
            return
        }

        currentDialog?.let {
            if (it.isAdded) {
                it.dismiss()
            }
        }

        val newDialog = dialogCreator.createDialog(
            context = activity,
            title = title,
            message = message,
            positiveText = positiveText,
            negativeText = negativeText,
            cancelable = cancelable,
            onPositiveClick = onPositiveListener,
            onNegativeClick = onNegativeListener,
            onDismiss = {
                onDismissListener?.invoke()
                currentDialog = null
            }
        )

        currentDialog = newDialog
        newDialog.show(activity.supportFragmentManager, newDialog::class.java.simpleName)
    }
}