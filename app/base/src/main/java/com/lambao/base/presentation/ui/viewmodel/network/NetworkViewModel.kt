package com.lambao.base.presentation.ui.viewmodel.network

import androidx.lifecycle.viewModelScope
import com.lambao.base.data.Resource
import com.lambao.base.data.remote.NetworkErrorType
import com.lambao.base.data.remote.NetworkException
import com.lambao.base.presentation.ui.viewmodel.BaseViewModel
import com.lambao.base.utils.log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
     * @param onLoading Callback executed when loading state is received (defaults to empty lambda)
     * @param onError Optional callback for handling errors, defaults to internal error handling if null
     * @param onSuccess Callback executed with the data when success state is received
     */
    protected fun <T> collectApi(
        flowUseCase: Flow<Resource<T>>,
        onLoading: () -> Unit = {},
        onError: ((Exception) -> Unit)? = null,
        onSuccess: (T?) -> Unit
    ) {
        flowUseCase.onEach { resource ->
            when (resource) {
                is Resource.Loading -> onLoading()
                is Resource.Success -> onSuccess.invoke(resource.data)
                is Resource.Error -> {
                    val exception = resource.throwable as Exception
                    onError?.invoke(exception) ?: handleError(resource)
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Collects a Flow of Resource from an API call and updates a StateFlow with the results.
     *
     * @param T The type of data wrapped in the Resource
     * @param flowUseCase The Flow emitting Resource states from an API call
     * @param stateFlow The MutableStateFlow to update with resource states
     */
    protected fun <T> collectApi(
        flowUseCase: Flow<Resource<T>>,
        stateFlow: MutableStateFlow<Resource<T>>
    ) {
        flowUseCase.onEach { resource ->
            stateFlow.emit(resource)
            when (resource) {
                is Resource.Loading -> log("Loading data...")
                is Resource.Success -> log("Data loaded: ${resource.data}")
                is Resource.Error -> handleError(resource)
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Collects a Flow of Resource from an API call, skipping loading state, and updates a StateFlow.
     *
     * @param T The type of data wrapped in the Resource
     * @param flowUseCase The Flow emitting Resource states from an API call
     * @param stateFlow The MutableStateFlow to update with non-loading resource states
     */
    protected fun <T> collectApiNoLoading(
        flowUseCase: Flow<Resource<T>>,
        stateFlow: MutableStateFlow<Resource<T>>
    ) {
        flowUseCase.onEach { resource ->
            if (resource !is Resource.Loading) {
                stateFlow.emit(resource)
                when (resource) {
                    is Resource.Success -> log("Data loaded: ${resource.data}")
                    is Resource.Error -> handleError(resource)
                    else -> Unit
                }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Handles error states from API calls by logging specific messages based on error type.
     *
     * @param T The type of data wrapped in the Resource
     * @param resource The Resource.Error containing the error information
     */
    protected fun <T> handleError(resource: Resource<T>) {
        when ((resource.throwable as? NetworkException)?.type) {
            NetworkErrorType.UNAUTHORIZED -> log("Error 401: Please login again")
            NetworkErrorType.NOT_FOUND -> log("Error 404: Data not found")
            NetworkErrorType.SERVER_ERROR -> log("Error 500: Server issue")
            NetworkErrorType.NO_NETWORK -> log("No internet connection")
            NetworkErrorType.TOO_MANY_REQUESTS -> log("Error 429: Too many requests")
            NetworkErrorType.BAD_REQUEST -> log("Error 400: Invalid request")
            NetworkErrorType.FORBIDDEN -> log("Error 403: Access denied")
            NetworkErrorType.BAD_GATEWAY -> log("Error 502: Bad gateway")
            NetworkErrorType.SERVICE_UNAVAILABLE -> log("Error 503: Service unavailable")
            NetworkErrorType.UNKNOWN -> log("Unknown error: ${resource.message}")
            null -> log("Unexpected error")
        }
    }
}