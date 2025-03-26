package com.lambao.base.extension

import com.lambao.base.data.Resource
import com.lambao.base.ui.activity.BaseActivity
import com.lambao.base.ui.bottom_sheet.BaseBottomSheet
import com.lambao.base.ui.dialog.BaseDialog
import com.lambao.base.ui.fragment.BaseFragment
import kotlinx.coroutines.flow.Flow

/**
 * Observes a Flow and executes the provided callback whenever a new value is emitted.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseActivity<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}

/**
 * Observes a Flow of Resource and handles loading, success, and error states.
 *
 * @param flow The Flow of Resource to observe
 * @param onLoading Optional callback for handling loading state, defaults to showing/hiding loading UI
 * @param onError Optional callback for handling error state
 * @param onSuccess Callback for handling successful data emission
 * @param T The type of data wrapped in the Resource
 */
fun <T> BaseActivity<*>.observeData(
    flow: Flow<Resource<T>>,
    onLoading: ((isLoading: Boolean) -> Unit)? = null,
    onError: ((Exception) -> Unit)? = null,
    onSuccess: (T?) -> Unit
) {
    flow.launchWhen(this) { resource ->
        when (resource) {
            is Resource.Loading -> onLoading?.invoke(true) ?: showLoading()
            is Resource.Success -> {
                onLoading?.invoke(false) ?: hideLoading()
                onSuccess(resource.data)
            }
            is Resource.Error -> {
                onLoading?.invoke(false) ?: hideLoading()
                onError?.invoke(resource.throwable as Exception)
            }
        }
    }
}

/**
 * Observes a Flow in a Fragment and executes the provided callback when new values are emitted.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseFragment<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}

/**
 * Observes a Flow of Resource in a Fragment and handles different resource states.
 *
 * @param flow The Flow of Resource to observe
 * @param onLoading Optional callback for handling loading state, defaults to showing/hiding loading UI
 * @param onError Optional callback for handling error state
 * @param onSuccess Callback for handling successful data emission
 * @param T The type of data wrapped in the Resource
 */
fun <T> BaseFragment<*>.observeData(
    flow: Flow<Resource<T>>,
    onLoading: ((isLoading: Boolean) -> Unit)? = null,
    onError: ((Exception) -> Unit)? = null,
    onSuccess: (T?) -> Unit
) {
    flow.launchWhen(this) { resource ->
        when (resource) {
            is Resource.Loading -> onLoading?.invoke(true) ?: showLoading()
            is Resource.Success -> {
                onLoading?.invoke(false) ?: hideLoading()
                onSuccess(resource.data)
            }
            is Resource.Error -> {
                onLoading?.invoke(false) ?: hideLoading()
                onError?.invoke(resource.throwable as Exception)
            }
        }
    }
}

/**
 * Observes a Flow in a Dialog and executes the callback when new values are emitted.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseDialog<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}

/**
 * Observes a Flow of Resource in a Dialog and handles different resource states.
 *
 * @param flow The Flow of Resource to observe
 * @param onLoading Optional callback for handling loading state, defaults to showing/hiding loading UI
 * @param onError Optional callback for handling error state
 * @param onSuccess Callback for handling successful data emission
 * @param T The type of data wrapped in the Resource
 */
fun <T> BaseDialog<*>.observeData(
    flow: Flow<Resource<T>>,
    onLoading: ((isLoading: Boolean) -> Unit)? = null,
    onError: ((Exception) -> Unit)? = null,
    onSuccess: (T?) -> Unit
) {
    flow.launchWhen(this) { resource ->
        when (resource) {
            is Resource.Loading -> onLoading?.invoke(true) ?: showLoading()
            is Resource.Success -> {
                onLoading?.invoke(false) ?: hideLoading()
                onSuccess(resource.data)
            }
            is Resource.Error -> {
                onLoading?.invoke(false) ?: hideLoading()
                onError?.invoke(resource.throwable as Exception)
            }
        }
    }
}

/**
 * Observes a Flow in a BottomSheet and executes the callback when new values are emitted.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseBottomSheet<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}

/**
 * Observes a Flow of Resource in a BottomSheet and handles different resource states.
 *
 * @param flow The Flow of Resource to observe
 * @param onLoading Optional callback for handling loading state, defaults to showing/hiding loading UI
 * @param onError Optional callback for handling error state
 * @param onSuccess Callback for handling successful data emission
 * @param T The type of data wrapped in the Resource
 */
fun <T> BaseBottomSheet<*>.observeData(
    flow: Flow<Resource<T>>,
    onLoading: ((isLoading: Boolean) -> Unit)? = null,
    onError: ((Exception) -> Unit)? = null,
    onSuccess: (T?) -> Unit
) {
    flow.launchWhen(this) { resource ->
        when (resource) {
            is Resource.Loading -> onLoading?.invoke(true) ?: showLoading()
            is Resource.Success -> {
                onLoading?.invoke(false) ?: hideLoading()
                onSuccess(resource.data)
            }
            is Resource.Error -> {
                onLoading?.invoke(false) ?: hideLoading()
                onError?.invoke(resource.throwable as Exception)
            }
        }
    }
}