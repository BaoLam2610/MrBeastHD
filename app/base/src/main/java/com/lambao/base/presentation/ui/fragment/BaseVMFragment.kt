package com.lambao.base.presentation.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.lambao.base.data.remote.NetworkException
import com.lambao.base.extension.observe
import com.lambao.base.presentation.ui.state.ScreenState
import com.lambao.base.presentation.ui.viewmodel.BaseViewModel

abstract class BaseVMFragment<B : ViewDataBinding, VM : BaseViewModel> : BaseFragment<B>() {

    protected lateinit var viewModel: VM

    protected abstract fun getViewModelClass(): Class<VM>

    protected abstract fun initObserve()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, getViewModelFactory())[getViewModelClass()]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScreenState()
        initObserve()
    }

    protected open fun initScreenState() {
        observe(viewModel.screenState) { state ->
            when (state) {
                is ScreenState.Loading -> showLoading()
                is ScreenState.Error -> {
                    hideLoading()
                    if (state.throwable is NetworkException) {
                        handleNetworkError(state.throwable)
                    }
                }

                else -> hideLoading()
            }
        }
    }

    protected open fun getViewModelFactory(): ViewModelProvider.Factory =
        defaultViewModelProviderFactory
}