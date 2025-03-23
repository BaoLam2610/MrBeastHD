package com.lambao.base.ui.viewmodel.network

import androidx.lifecycle.viewModelScope
import com.lambao.base.data.Resource
import com.lambao.base.utils.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

open class MultiNetworkViewModel : NetworkViewModel() {

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
                }
            }
        }.launchIn(viewModelScope)
    }

    protected fun <T> collectApis(
        vararg flows: Flow<Resource<T>>,
        onResults: (List<T>) -> Unit,
        onError: ((Exception) -> Unit)? = null
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

    /** call multiple api and ignore error */
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

    /** call multiple api and ignore error */
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