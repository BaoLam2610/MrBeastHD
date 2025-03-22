package com.lambao.base.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseViewModelFragment<B : ViewDataBinding, VM : ViewModel> : BaseFragment<B>() {

    @Inject
    lateinit var viewModel: VM

    abstract fun initObserve()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }
}