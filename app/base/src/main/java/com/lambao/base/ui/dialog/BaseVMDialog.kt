package com.lambao.base.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseVMDialog<B : ViewDataBinding, VM : ViewModel> : BaseDialog<B>() {

    @Inject
    lateinit var viewModel: VM

    protected abstract fun initObserve()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }
}