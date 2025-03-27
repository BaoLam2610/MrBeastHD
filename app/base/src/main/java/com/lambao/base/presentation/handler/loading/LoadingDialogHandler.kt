package com.lambao.base.presentation.handler.loading

import android.content.Context
import com.lambao.base.presentation.ui.view.loading.LoadingDialog

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