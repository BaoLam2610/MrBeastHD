package com.lambao.mrbeast.presentation.binding

import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.lambao.mrbeast.presentation.ui.view.toolbar.CommonToolbar

object ToolbarBindingAdapters {
    @JvmStatic
    @BindingAdapter("title")
    fun setToolbarTitle(toolbar: CommonToolbar, title: String?) {
        toolbar.title = title ?: ""
    }

    @JvmStatic
    @BindingAdapter("showBackButton")
    fun setShowBackButton(toolbar: CommonToolbar, isShow: Boolean) {
        toolbar.showBackButton = isShow
    }

    @JvmStatic
    @BindingAdapter("showActionButton")
    fun setShowActionButton(toolbar: CommonToolbar, isShow: Boolean) {
        toolbar.showActionButton = isShow
    }

    @JvmStatic
    @BindingAdapter("toolbarBackground")
    fun setToolbarBackground(toolbar: CommonToolbar, resId: Int?) {
        resId?.let {
            toolbar.toolbarBackground = ContextCompat.getDrawable(toolbar.context, it)
        }
    }

    @JvmStatic
    @BindingAdapter("srcBackIcon")
    fun setBackIcon(toolbar: CommonToolbar, resId: Int?) {
        resId?.let {
            toolbar.backIcon = ContextCompat.getDrawable(toolbar.context, it)
        }
    }

    @JvmStatic
    @BindingAdapter("srcActionIcon")
    fun setActionIcon(toolbar: CommonToolbar, resId: Int?) {
        resId?.let {
            toolbar.actionIcon = ContextCompat.getDrawable(toolbar.context, it)
        }
    }

    @JvmStatic
    @BindingAdapter("onActionClick")
    fun setOnActionClickListener(toolbar: CommonToolbar, listener: (() -> Unit)?) {
        listener?.let { toolbar.setOnActionClickListener(it) }
    }
}