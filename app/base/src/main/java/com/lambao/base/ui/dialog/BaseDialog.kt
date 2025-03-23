package com.lambao.base.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.lambao.base.ui.view.loading.LoadingDialogHandler
import com.lambao.base.ui.view.loading.LoadingHandler
import kotlinx.coroutines.CoroutineScope

abstract class BaseDialog<B : ViewDataBinding> : DialogFragment() {

    private var _binding: B? = null
    protected val binding: B
        get() = _binding
            ?: throw IllegalStateException("Binding in ${this::class.java.simpleName} is null")

    protected open val loadingHandler: LoadingHandler by lazy {
        LoadingDialogHandler(requireActivity())
    }

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

    protected fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenCreated(block)
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

    protected fun showLoading() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            loadingHandler.showLoading()
        }
    }

    protected fun hideLoading() {
        loadingHandler.hideLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoading()
        _binding = null
    }
}