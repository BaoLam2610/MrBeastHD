package com.lambao.base.presentation.ui.viewmodel.network

import androidx.lifecycle.viewModelScope
import com.lambao.base.data.Resource
import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Open ViewModel class extending BaseViewModel, providing utilities for handling network API flows.
 */
open class NetworkViewModel(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseViewModel(ioDispatcher, defaultDispatcher) {

    /**
     * Collects a Flow of Resource from an API call with customizable callbacks for different states.
     *
     * @param T The type of data wrapped in the Resource
     * @param flowUseCase The Flow emitting Resource states from an API call
     * @param onError Optional callback for handling errors, defaults to internal error handling if null
     * @param onSuccess Callback executed with the data when success state is received
     */
    protected fun <T> collectApi(
        flowUseCase: Flow<Resource<T>>,
        onError: ((Throwable) -> Unit)? = null,
        onSuccess: suspend (T) -> Unit
    ) {
        flowUseCase.onEach { resource ->
            when (resource) {
                is Resource.Loading -> setLoadingScreenState()
                is Resource.Success -> {
                    resource.data?.let {
                        setSuccessScreenState()
                        onSuccess.invoke(it)
                    } ?: run {
                        val errorMessage = "Data is null"
                        setErrorScreenState(Exception(errorMessage))
                        onError?.invoke(Exception(errorMessage))
                    }
                }

                is Resource.Error -> {
                    val throwable = resource.throwable ?: Exception("Unknown error")
                    setErrorScreenState(throwable)
                    onError?.invoke(throwable)
                }

                else -> setIdleScreenState()
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Collects a Flow of Resource from an API call, skipping loading state, and updates a StateFlow.
     *
     * @param T The type of data wrapped in the Resource
     * @param flowUseCase The Flow emitting Resource states from an API call
     * @param onError Optional callback for handling errors, defaults to internal error handling if null
     * @param onSuccess Callback executed with the data when success state is received
     */
    protected fun <T> collectApiNoLoading(
        flowUseCase: Flow<Resource<T>>,
        onError: ((Throwable) -> Unit)? = null,
        onSuccess: suspend (T) -> Unit
    ) {
        flowUseCase.onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        setSuccessScreenState()
                        onSuccess.invoke(it)
                    } ?: run {
                        val errorMessage = "Data is null"
                        setErrorScreenState(Exception(errorMessage))
                        onError?.invoke(Exception(errorMessage))
                    }
                }

                is Resource.Error -> {
                    val throwable = resource.throwable ?: Exception("Unknown error")
                    setErrorScreenState(throwable)
                    onError?.invoke(throwable)
                }

                else -> setIdleScreenState()
            }
        }.launchIn(viewModelScope)
    }
}