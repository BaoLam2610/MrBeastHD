package com.lambao.base.ui.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.widget.FrameLayout

abstract class BaseCustomView<B : ViewDataBinding> : FrameLayout {

    private var _binding: ViewDataBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: B
        get() = _binding as B

    constructor(context: Context) : super(context) {
        initBinding()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initBinding()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initBinding()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    private fun initBinding() {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            getLayoutId(),
            this,
            true
        )
    }
}