package com.lambao.base.presentation.ui.custom_view

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lambao.base.extension.restoreInstanceState
import com.lambao.base.extension.saveInstanceState

abstract class BaseCustomView<B : ViewDataBinding> : FrameLayout {

    private var _binding: B? = null
    protected val binding: B
        get() = _binding
            ?: throw IllegalStateException("Binding in ${this::class.java.simpleName} is null")

    constructor(context: Context) : super(context) {
        initBinding()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initBinding()
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initBinding()
        initView(attrs)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    protected abstract fun initView(attrs: AttributeSet)

    private fun initBinding() {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            getLayoutId(),
            this,
            true
        )
    }

    override fun onSaveInstanceState(): Parcelable? {
        return saveInstanceState(super.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(restoreInstanceState(state))
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }
}