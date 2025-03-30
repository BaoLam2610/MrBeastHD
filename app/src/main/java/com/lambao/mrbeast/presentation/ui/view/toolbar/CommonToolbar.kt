package com.lambao.mrbeast.presentation.ui.view.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.lambao.base.extension.click
import com.lambao.base.presentation.ui.custom_view.BaseCustomView
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.LayoutToolbarBinding

class CommonToolbar : BaseCustomView<LayoutToolbarBinding> {

    var title: String
        get() = binding.tvTitle.text.toString()
        set(value) {
            binding.tvTitle.text = value
        }

    var showBackButton: Boolean
        get() = binding.btnBack.isVisible
        set(value) {
            binding.btnBack.isVisible = value
        }

    var showActionButton: Boolean
        get() = binding.btnAction.isVisible
        set(value) {
            binding.btnAction.isVisible = value
        }

    var toolbarBackground: Drawable?
        get() = try {
            binding.layoutContainer.background
        } catch (_: Exception) {
            null
        }
        set(value) {
            if (value != null) {
                binding.layoutContainer.background = value
            }
        }

    var backIcon: Drawable?
        get() = try {
            binding.btnBack.background
        } catch (_: Exception) {
            null
        }
        set(value) {
            if (value != null) {
                binding.btnBack.background = value
            }
        }

    var actionIcon: Drawable?
        get() = try {
            binding.btnAction.background
        } catch (_: Exception) {
            null
        }
        set(value) {
            if (value != null) {
                binding.btnAction.background = value
            }
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getLayoutId() = R.layout.layout_toolbar

    override fun initView(attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.CommonToolbar).run {
            title = getString(R.styleable.CommonToolbar_title) ?: ""
            showBackButton = getBoolean(R.styleable.CommonToolbar_showBackButton, true)
            showActionButton = getBoolean(R.styleable.CommonToolbar_showActionButton, false)
            toolbarBackground = ContextCompat.getDrawable(
                context,
                getResourceId(R.styleable.CommonToolbar_background, 0)
            )
            backIcon = ContextCompat.getDrawable(
                context,
                getResourceId(R.styleable.CommonToolbar_srcBackIcon, 0)
            )
            actionIcon = ContextCompat.getDrawable(
                context,
                getResourceId(R.styleable.CommonToolbar_srcActionIcon, 0)
            )

            recycle()
        }
    }

    override fun setBackground(drawable: Drawable?) {
        this.toolbarBackground = drawable
    }

    fun setOnActionClickListener(listener: () -> Unit) {
        binding.btnAction.click { listener() }
    }
}