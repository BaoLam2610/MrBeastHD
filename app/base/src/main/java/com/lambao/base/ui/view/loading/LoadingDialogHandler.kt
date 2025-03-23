package com.lambao.base.ui.view.loading

import android.content.Context

class LoadingDialogHandler(private val context: Context) : LoadingHandler {
    private var dialog: LoadingDialog? = null

    override fun showLoading() {
        if (dialog == null || !dialog!!.isShowing) {
            dialog = LoadingDialog(context).apply {
                show()
            }
        }
    }

    override fun hideLoading() {
        dialog?.dismiss()
        dialog = null
    }
}