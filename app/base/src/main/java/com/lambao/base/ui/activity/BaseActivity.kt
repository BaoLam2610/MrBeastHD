package com.lambao.base.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.lambao.base.ui.view.loading.LoadingDialogHandler
import com.lambao.base.ui.view.loading.LoadingHandler
import kotlinx.coroutines.CoroutineScope

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    private var _binding: B? = null
    protected val binding: B
        get() = _binding
            ?: throw IllegalStateException("Binding in ${this::class.java.simpleName} is null")

    protected open val loadingHandler: LoadingHandler by lazy {
        LoadingDialogHandler(this)
    }

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    protected abstract fun onViewReady(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, getLayoutResId())
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        onViewReady(savedInstanceState)
    }

    protected fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenCreated(block)
    }

    fun showLoading() {
        loadingHandler.showLoading()
    }

    fun hideLoading() {
        loadingHandler.hideLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) hideLoading()
        _binding = null
    }
}