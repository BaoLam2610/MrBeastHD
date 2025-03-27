package com.lambao.base.presentation.ui.viewmodel.network

import androidx.lifecycle.viewModelScope
import com.lambao.base.data.Resource
import com.lambao.base.utils.log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Open ViewModel class extending NetworkViewModel, providing utilities for handling multiple network API flows concurrently.
 */
open class MultiNetworkViewModel(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : NetworkViewModel(ioDispatcher, defaultDispatcher) {

    /**
     * Collects multiple API Flows and provides all results as a list of Resources.
     *
     * @param T The type of data wrapped in the Resources
     * @param flows Variable number of Flows emitting Resource states from API calls
     * @param onResults Callback executed with the list of all Resource results
     */
    protected fun <T> collectApis(
        vararg flows: Flow<Resource<T>>,
        onResults: (List<Resource<T>>) -> Unit
    ) {
        combine(flows.toList()) { resources ->
            resources.toList()
        }.onEach { resources ->
            onResults(resources)
            resources.forEach { resource ->
                when (resource) {
                    is Resource.Loading -> log("Loading: $resource")
                    is Resource.Success -> log("Success: ${resource.data}")
                    is Resource.Error -> handleError(resource)
                    else -> Unit
                }
            }
        }.launchIn(viewModelScope)
    }

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
        onError: ((Exception) -> Unit)? = null,
        onResults: (List<T>) -> Unit
    ) {
        combine(flows.toList()) { resources ->
            resources.toList()
        }.onEach { resources ->
            val allSuccess = resources.all { it is Resource.Success }
            if (allSuccess) {
                val dataList = resources.mapNotNull { (it as Resource.Success).data }
                onResults(dataList)
            } else {
                resources.firstOrNull { it is Resource.Error }?.let { error ->
                    val exception = error.throwable as Exception
                    onError?.invoke(exception) ?: handleError(error)
                }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Collects multiple API Flows, ignoring errors and providing only successful results.
     *
     * Calls multiple APIs and ignores errors, passing only successful Resources to the callback.
     *
     * @param T The type of data wrapped in the Resources
     * @param flows Variable number of Flows emitting Resource states from API calls
     * @param onResults Callback executed with list of successful Resource results
     */
    protected fun <T> collectSafeApis(
        vararg flows: Flow<Resource<T>>,
        onResults: (List<Resource<T>>) -> Unit
    ) {
        combine(flows.toList()) { resources ->
            resources.toList()
        }.onEach { resources ->
            val successData = resources
                .filterIsInstance<Resource.Success<T>>()
            if (successData.isNotEmpty()) {
                onResults(successData)
            }
            resources.forEach { resource ->
                if (resource is Resource.Error) {
                    handleError(resource)
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
            val successData = resources
                .filterIsInstance<Resource.Success<T>>()
                .mapNotNull { it.data }
            if (successData.isNotEmpty()) {
                onResults(successData)
            }
            resources.forEach { resource ->
                if (resource is Resource.Error) {
                    handleError(resource)
                }
            }
        }.launchIn(viewModelScope)
    }
}