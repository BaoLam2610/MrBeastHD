package com.lambao.base.ui.viewmodel.network

import androidx.lifecycle.viewModelScope
import com.lambao.base.data.Resource
import com.lambao.base.data.remote.NetworkErrorType
import com.lambao.base.data.remote.NetworkException
import com.lambao.base.ui.viewmodel.BaseViewModel
import com.lambao.base.utils.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

open class NetworkViewModel : BaseViewModel() {

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
            }
        }.launchIn(viewModelScope)
    }

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
            }
        }.launchIn(viewModelScope)
    }

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