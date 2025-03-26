package com.lambao.base.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Open base ViewModel class providing coroutine launching utilities with configurable dispatchers.
 *
 * @param ioDispatcher The CoroutineDispatcher for IO-bound operations (defaults to Dispatchers.IO)
 * @param defaultDispatcher The CoroutineDispatcher for general operations (defaults to Dispatchers.Default)
 */
open class BaseViewModel(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    /**
     * Launches a coroutine in the ViewModel scope using the IO dispatcher.
     * Suitable for IO-bound operations like network calls or database access.
     *
     * @param block The suspend function to execute within the coroutine scope
     * @return A Job representing the launched coroutine
     */
    protected fun launchIo(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            block()
        }
    }

    /**
     * Launches a coroutine in the ViewModel scope using the default dispatcher.
     * Suitable for general-purpose operations.
     *
     * @param block The suspend function to execute within the coroutine scope
     * @return A Job representing the launched coroutine
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(defaultDispatcher) {
            block()
        }
    }
}