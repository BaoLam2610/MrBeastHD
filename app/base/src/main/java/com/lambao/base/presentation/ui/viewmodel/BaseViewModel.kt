package com.lambao.base.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lambao.base.data.Resource
import com.lambao.base.presentation.ui.state.ScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Base ViewModel providing utilities for managing screen state and launching coroutines with configurable dispatchers.
 * This class simplifies common ViewModel operations like handling data flows, screen states, and coroutine scopes.
 *
 * @param ioDispatcher The [CoroutineDispatcher] for IO-bound operations (defaults to [Dispatchers.IO]).
 * @param defaultDispatcher The [CoroutineDispatcher] for general-purpose operations (defaults to [Dispatchers.Default]).
 * @param mainDispatcher The [CoroutineDispatcher] for UI-related operations (defaults to [Dispatchers.Main]).
 */
open class BaseViewModel(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Idle())
    val screenState: StateFlow<ScreenState> get() = _screenState

    /**
     * Sets the screen state to the specified [ScreenState].
     *
     * @param state The [ScreenState] to set (e.g., Idle, Loading, Success, Error).
     */
    fun setScreenState(state: ScreenState) {
        viewModelScope.launch(mainDispatcher) {
            _screenState.emit(state)
        }
    }

    /** Sets the screen state to [ScreenState.Idle]. */
    fun setIdleScreenState() {
        setScreenState(ScreenState.Idle())
    }

    /** Sets the screen state to [ScreenState.Loading]. */
    fun setLoadingScreenState() {
        setScreenState(ScreenState.Loading())
    }

    /** Sets the screen state to [ScreenState.Success]. */
    fun setSuccessScreenState() {
        setScreenState(ScreenState.Success())
    }

    /**
     * Sets the screen state to [ScreenState.Error] with the provided [throwable].
     *
     * @param throwable The error cause to associate with the error state.
     */
    fun setErrorScreenState(throwable: Throwable) {
        setScreenState(ScreenState.Error(throwable))
    }

    /**
     * Provides the default error message when data is null.
     * Subclasses can override this to customize the message.
     *
     * @return The default message for null data errors.
     */
    protected open fun getDataIsNullMessage(): String = "Data is null"

    /**
     * Provides the default error message for unknown errors.
     * Subclasses can override this to customize the message.
     *
     * @return The default message for unknown errors.
     */
    protected open fun getUnknownErrorMessage(): String = "Unknown error"

    /**
     * Launches a coroutine in the [viewModelScope] using the IO dispatcher.
     * Suitable for IO-bound operations like network calls or database access.
     *
     * @param block The suspend function to execute within the coroutine scope.
     * @return A [Job] representing the launched coroutine.
     */
    protected fun launchIo(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(ioDispatcher) {
            block()
        }
    }

    /**
     * Launches a coroutine in the [viewModelScope] using the default dispatcher.
     * Suitable for general-purpose operations like computations.
     *
     * @param block The suspend function to execute within the coroutine scope.
     * @return A [Job] representing the launched coroutine.
     */
    protected fun launchDefault(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(defaultDispatcher) {
            block()
        }
    }

    /**
     * Launches a coroutine in the [viewModelScope] using the main dispatcher.
     * Suitable for updating UI state or interacting with Android UI components.
     *
     * @param block The suspend function to execute within the coroutine scope.
     * @return A [Job] representing the launched coroutine.
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(mainDispatcher) {
            block()
        }
    }

    /**
     * Handles a [Flow] of [Resource] without emitting a loading state.
     * Updates the [screenState] and invokes callbacks based on the resource state.
     *
     * @param flowUseCase The [Flow] emitting [Resource] objects.
     * @param onError Optional callback invoked when an error occurs.
     * @param onSuccess Callback invoked with the data when the resource is successful.
     */
    protected fun <T> handleDataNoLoading(
        flowUseCase: Flow<Resource<T>>,
        onError: ((Throwable) -> Unit)? = null,
        onSuccess: suspend (T) -> Unit
    ) {
        flowUseCase.onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        setSuccessScreenState()
                        onSuccess(it)
                    } ?: run {
                        val error = Exception(getDataIsNullMessage())
                        setErrorScreenState(error)
                        onError?.invoke(error)
                    }
                }
                is Resource.Error -> {
                    val throwable = resource.throwable ?: Exception(getUnknownErrorMessage())
                    setErrorScreenState(throwable)
                    onError?.invoke(throwable)
                }
                else -> setIdleScreenState()
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Handles a [Flow] of [Resource] with loading state support.
     * Updates the [screenState] and invokes callbacks based on the resource state.
     *
     * @param flowUseCase The [Flow] emitting [Resource] objects.
     * @param onError Optional callback invoked when an error occurs.
     * @param onSuccess Callback invoked with the data when the resource is successful.
     */
    protected fun <T> handleData(
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
                        onSuccess(it)
                    } ?: run {
                        val error = Exception(getDataIsNullMessage())
                        setErrorScreenState(error)
                        onError?.invoke(error)
                    }
                }
                is Resource.Error -> {
                    val throwable = resource.throwable ?: Exception(getUnknownErrorMessage())
                    setErrorScreenState(throwable)
                    onError?.invoke(throwable)
                }
                else -> setIdleScreenState()
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Combines multiple [Flow]s of [Resource] and processes them when all succeed.
     * Updates the [screenState] and invokes callbacks based on the combined results.
     *
     * @param flows Vararg of [Flow]s emitting [Resource] objects.
     * @param onError Optional callback invoked when any flow emits an error.
     * @param onResults Callback invoked with the list of successful data.
     */
    protected fun <T> handleMultiData(
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
                if (dataList.isNotEmpty()) {
                    setSuccessScreenState()
                    onResults(dataList)
                } else {
                    val error = Exception(getDataIsNullMessage())
                    setErrorScreenState(error)
                    onError?.invoke(error)
                }
            } else {
                resources.firstOrNull { it is Resource.Error }?.let { errorResource ->
                    val throwable = (errorResource as Resource.Error).throwable
                        ?: Exception(getUnknownErrorMessage())
                    setErrorScreenState(throwable)
                    onError?.invoke(throwable)
                }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Combines multiple [Flow]s of [Resource] and processes successful data, ignoring errors.
     * Updates the [screenState] and invokes the callback with successful data.
     *
     * @param flows Vararg of [Flow]s emitting [Resource] objects.
     * @param onResults Callback invoked with the list of successful data.
     */
    protected fun <T> handleMultiDataIgnoreErrors(
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
            } else {
                setIdleScreenState()
            }
        }.launchIn(viewModelScope)
    }
}