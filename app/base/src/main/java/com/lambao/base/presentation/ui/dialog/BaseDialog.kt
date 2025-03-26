package com.lambao.base.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import com.lambao.base.data.remote.NetworkException
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.presentation.handler.dialog.DialogHandlerImpl
import com.lambao.base.presentation.handler.network_error.NetworkErrorHandler
import com.lambao.base.presentation.handler.network_error.NetworkErrorHandlerImpl
import com.lambao.base.presentation.handler.permission.common.PermissionHandlerFactory
import com.lambao.base.presentation.handler.permission.common.SpecificPermissionHandler
import com.lambao.base.presentation.ui.view.loading.LoadingDialogHandler
import com.lambao.base.presentation.ui.view.loading.LoadingHandler

abstract class BaseDialog<B : ViewDataBinding> : DialogFragment() {

    private var _binding: B? = null
    protected val binding: B
        get() = _binding
            ?: throw IllegalStateException("Binding in ${this::class.java.simpleName} is null")

    protected open val dialogHandler: DialogHandler by lazy {
        DialogHandlerImpl(requireActivity())
    }

    protected open val loadingHandler: LoadingHandler by lazy {
        LoadingDialogHandler(requireActivity())
    }

    protected open val networkErrorHandler: NetworkErrorHandler by lazy {
        NetworkErrorHandlerImpl(
            requireContext(),
            dialogHandler
        )
    }

    protected fun getPermissionHandler(type: PermissionHandlerFactory.PermissionType): SpecificPermissionHandler =
        PermissionHandlerFactory.getHandler(type, requireActivity(), dialogHandler)

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun onViewReady(savedInstance: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady(savedInstanceState)
    }

    protected fun setFull(isFull: Boolean) {
        dialog?.let {
            if (isFull) {
                it.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            } else {
                it.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    fun showLoading() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            loadingHandler.showLoading()
        }
    }

    fun hideLoading() {
        loadingHandler.hideLoading()
    }

    fun handleNetworkError(networkException: NetworkException) {
        networkErrorHandler.handleError(networkException)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoading()
        _binding = null
    }
}