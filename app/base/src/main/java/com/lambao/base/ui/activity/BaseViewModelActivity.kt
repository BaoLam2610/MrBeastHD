package com.lambao.base.ui.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseViewModelActivity<B : ViewDataBinding, VM : ViewModel> : BaseActivity<B>() {

    @Inject
    lateinit var viewModel: VM
    
    protected abstract fun initObserve()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserve()
    }
}