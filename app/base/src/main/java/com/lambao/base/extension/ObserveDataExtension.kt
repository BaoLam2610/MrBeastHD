package com.lambao.base.extension

import com.lambao.base.data.Resource
import com.lambao.base.ui.activity.BaseActivity
import com.lambao.base.ui.bottom_sheet.BaseBottomSheet
import com.lambao.base.ui.dialog.BaseDialog
import com.lambao.base.ui.fragment.BaseFragment
import kotlinx.coroutines.flow.Flow

/* Observe data in BaseActivity */
fun <T> BaseActivity<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}

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

/* Observe data in BaseFragment */
fun <T> BaseFragment<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}

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

/* Observe data in BaseDialog */
fun <T> BaseDialog<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}

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

/* Observe data in BaseBottomSheet */
fun <T> BaseBottomSheet<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}

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