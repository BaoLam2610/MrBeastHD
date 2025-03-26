package com.lambao.base.ui.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.lambao.base.ui.viewmodel.BaseViewModel

abstract class BaseVMFragment<B : ViewDataBinding, VM : BaseViewModel> : BaseFragment<B>() {

    protected lateinit var viewModel: VM

    protected abstract fun getViewModelClass(): Class<VM>

    protected abstract fun initObserve()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, getViewModelFactory())[getViewModelClass()]
        initObserve()
    }

    protected open fun getViewModelFactory(): ViewModelProvider.Factory =
        defaultViewModelProviderFactory
}