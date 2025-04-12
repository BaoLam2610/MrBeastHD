package com.lambao.base.presentation.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lambao.base.R
import com.lambao.base.data.remote.NetworkException
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.presentation.handler.dialog.DialogHandlerImpl
import com.lambao.base.presentation.handler.loading.LoadingDialogHandler
import com.lambao.base.presentation.handler.loading.LoadingHandler
import com.lambao.base.presentation.handler.network_error.NetworkErrorHandler
import com.lambao.base.presentation.handler.network_error.NetworkErrorHandlerImpl
import com.lambao.base.presentation.handler.permission.DynamicPermissionHandler
import com.lambao.base.presentation.handler.permission.common.SpecificPermissionHandler

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    private var _binding: B? = null
    protected val binding: B
        get() = _binding
            ?: throw IllegalStateException("Binding in ${this::class.java.simpleName} is null")

    protected open val dialogHandler: DialogHandler by lazy {
        DialogHandlerImpl(this)
    }

    protected open val loadingHandler: LoadingHandler by lazy {
        LoadingDialogHandler(this)
    }

    protected open val networkErrorHandler: NetworkErrorHandler by lazy {
        NetworkErrorHandlerImpl(
            this,
            dialogHandler
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

    protected open fun getPermissionHandler(
        vararg permissions: String,
        permissionDescription: String
    ): SpecificPermissionHandler? {
        try {
            if (permissions.isEmpty()) {
                dialogHandler.showAlertDialog(getString(R.string.at_least_one_permission_must_be_provided))
                return null
            }
            return DynamicPermissionHandler(
                this,
                dialogHandler,
                permissions.toList(),
                permissionDescription
            )
        } catch (e: Exception) {
            e.printStackTrace()
            e.message?.let { dialogHandler.showAlertDialog(it) }
            return null
        }
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