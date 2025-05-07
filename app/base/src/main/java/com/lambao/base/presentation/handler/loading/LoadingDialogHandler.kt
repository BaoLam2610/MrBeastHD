package com.lambao.base.presentation.handler.loading

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.lambao.base.presentation.ui.view.loading.LoadingDialog

class LoadingDialogHandler(private val activity: FragmentActivity) : LoadingHandler {
    private var dialog: LoadingDialog? = null

    override fun showLoading() {
        if (!activity.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
            return

        if (dialog == null || !dialog!!.isShowing) {
            dialog = LoadingDialog(activity).apply {
                show()
            }
        }
    }

    override fun hideLoading() {
        dialog?.dismiss()
        dialog = null
    }
}