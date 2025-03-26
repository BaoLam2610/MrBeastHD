package com.lambao.base.ui.dialog

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle

class DialogHandlerImpl(
    private val activity: FragmentActivity
) : DialogHandler {
    override fun showMessageDialog(message: String) {
        if (activity.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            AlertDialog.Builder(activity)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .setCancelable(false)
                .show()
        }
    }
}