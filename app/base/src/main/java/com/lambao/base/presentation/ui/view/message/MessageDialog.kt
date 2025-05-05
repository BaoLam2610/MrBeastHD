package com.lambao.base.presentation.ui.view.message

import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import com.lambao.base.R
import com.lambao.base.databinding.LayoutMessageDialogBinding
import com.lambao.base.extension.click
import com.lambao.base.presentation.ui.dialog.BaseDialog

class MessageDialog(
    private val title: String?,
    private val message: String,
    private val positiveText: String?,
    private val negativeText: String?,
    private val cancelable: Boolean,
    private val onPositiveClick: (() -> Unit)?,
    private val onNegativeClick: (() -> Unit)?,
    private val onDismiss: (() -> Unit)?
) : BaseDialog<LayoutMessageDialogBinding>() {

    companion object {
        fun newInstance(
            title: String?,
            message: String,
            positiveText: String?,
            negativeText: String?,
            cancelable: Boolean,
            onPositiveClick: (() -> Unit)?,
            onNegativeClick: (() -> Unit)?,
            onDismiss: (() -> Unit)?
        ) = MessageDialog(
            title,
            message,
            positiveText,
            negativeText,
            cancelable,
            onPositiveClick,
            onNegativeClick,
            onDismiss
        ).apply {
            setStyle(STYLE_NO_TITLE, R.style.transparent_dialog)
        }
    }

    override fun getLayoutId() = R.layout.layout_message_dialog

    override fun onViewReady(savedInstance: Bundle?) {
        with(binding) {
            title = this@MessageDialog.title
            message = this@MessageDialog.message
            positiveText = this@MessageDialog.positiveText
            negativeText = this@MessageDialog.negativeText
            isCancelable = this@MessageDialog.cancelable

            btnClose.click {
                onNegativeClick?.invoke()
                dismissAllowingStateLoss()
            }

            btnPositive.click {
                onPositiveClick?.invoke()
                dismissAllowingStateLoss()
            }

            btnNegative.click {
                onNegativeClick?.invoke()
                dismissAllowingStateLoss()
            }
        }
        val window: Window? = dialog?.window
        val displayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels * 0.95).toInt()
        window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }
}