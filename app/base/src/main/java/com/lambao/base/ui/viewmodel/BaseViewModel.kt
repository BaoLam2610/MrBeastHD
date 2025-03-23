package com.lambao.base.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lambao.base.di.DefaultDispatcher
import com.lambao.base.di.IODispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel : ViewModel() {

    @Inject
    @IODispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    @DefaultDispatcher
    lateinit var defaultDispatcher: CoroutineDispatcher

    protected fun launchIo(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            block()
        }
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(defaultDispatcher) {
            block()
        }
    }
}