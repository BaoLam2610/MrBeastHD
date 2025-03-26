package com.lambao.base.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel(
    protected val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    protected val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

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