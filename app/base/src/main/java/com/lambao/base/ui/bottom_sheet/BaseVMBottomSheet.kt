package com.lambao.base.ui.bottom_sheet

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseVMBottomSheet<B : ViewDataBinding, VM : ViewModel> : BaseBottomSheet<B>() {

    @Inject
    lateinit var viewModel: VM

    protected abstract fun initObserve()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }
}