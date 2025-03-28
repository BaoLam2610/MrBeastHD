package com.lambao.base.presentation.ui.viewmodel.network

import androidx.lifecycle.viewModelScope
import com.lambao.base.data.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Open ViewModel class extending NetworkViewModel, providing utilities for handling multiple network
 * API flows concurrently.
 */
open class MultiNetworkViewModel(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : NetworkViewModel(ioDispatcher, defaultDispatcher) {

    /**
     * Collects multiple API Flows, invoking onResults only if all succeed, or handling first error.
     *
     * @param T The type of data wrapped in the Resources
     * @param flows Variable number of Flows emitting Resource states from API calls
     * @param onError Optional callback for handling the first encountered error
     * @param onResults Callback executed with list of successful data when all calls succeed
     */
    protected fun <T> collectApis(
        vararg flows: Flow<Resource<T>>,
        onError: ((Throwable) -> Unit)? = null,
        onResults: (List<T>) -> Unit
    ) {
        combine(flows.toList()) { resources ->
            resources.toList()
        }.onEach { resources ->
            setLoadingScreenState()
            val allSuccess = resources.all { it is Resource.Success }
            if (allSuccess) {
                val dataList = resources.mapNotNull { (it as Resource.Success).data }
                setSuccessScreenState()
                onResults(dataList)
            } else {
                resources.firstOrNull { it is Resource.Error }?.let { error ->
                    val throwable = error.throwable ?: Exception("Unknown error")
                    setErrorScreenState(throwable)
                    onError?.invoke(throwable)
                } ?: run {
                    setIdleScreenState()
                }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Collects multiple API Flows, ignoring errors and providing only successful data.
     *
     * Calls multiple APIs and ignores errors, passing only successful data to the callback.
     *
     * @param T The type of data wrapped in the Resources
     * @param flows Variable number of Flows emitting Resource states from API calls
     * @param onResults Callback executed with list of successful data when any calls succeed
     */
    protected fun <T> collectApisIgnoreErrors(
        vararg flows: Flow<Resource<T>>,
        onResults: (List<T>) -> Unit
    ) {
        combine(flows.toList()) { resources ->
            resources.toList()
        }.onEach { resources ->
            setLoadingScreenState()
            val successData = resources
                .filterIsInstance<Resource.Success<T>>()
                .mapNotNull { it.data }
            if (successData.isNotEmpty()) {
                setSuccessScreenState()
                onResults(successData)
                return@onEach
            }
            setIdleScreenState()
        }.launchIn(viewModelScope)
    }
}