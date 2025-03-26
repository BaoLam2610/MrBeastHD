package com.lambao.base.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lambao.base.data.remote.NetworkException
import com.lambao.base.ui.dialog.DialogHandlerImpl
import com.lambao.base.ui.error_handler.NetworkErrorHandler
import com.lambao.base.ui.error_handler.NetworkErrorHandlerImpl
import com.lambao.base.ui.view.loading.LoadingDialogHandler
import com.lambao.base.ui.view.loading.LoadingHandler

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    private var _binding: B? = null
    protected val binding: B
        get() = _binding
            ?: throw IllegalStateException("Binding in ${this::class.java.simpleName} is null")

    protected open val loadingHandler: LoadingHandler by lazy {
        LoadingDialogHandler(this)
    }

    protected open val networkErrorHandler: NetworkErrorHandler by lazy {
        NetworkErrorHandlerImpl(
            this,
            DialogHandlerImpl(this)
        )
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

    fun showLoading() {
        loadingHandler.showLoading()
    }

    fun hideLoading() {
        loadingHandler.hideLoading()
    }

    fun handleNetworkError(networkException: NetworkException) {
        networkErrorHandler.handleError(networkException)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) hideLoading()
        _binding = null
    }
}