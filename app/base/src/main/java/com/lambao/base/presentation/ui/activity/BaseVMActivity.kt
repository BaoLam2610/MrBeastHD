package com.lambao.base.presentation.ui.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.lambao.base.data.remote.NetworkException
import com.lambao.base.extension.observeLatest
import com.lambao.base.presentation.ui.state.ScreenState
import com.lambao.base.presentation.ui.viewmodel.BaseViewModel

abstract class BaseVMActivity<B : ViewDataBinding, VM : BaseViewModel> : BaseActivity<B>() {

    protected lateinit var viewModel: VM

    protected abstract fun getViewModelClass(): Class<VM>

    protected abstract fun initObserve()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, getViewModelFactory())[getViewModelClass()]
        initObserve()
        initScreenState()
    }

    protected open fun initScreenState() {
        observeLatest(viewModel.screenState) { state ->
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