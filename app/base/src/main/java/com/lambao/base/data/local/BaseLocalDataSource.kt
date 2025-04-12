package com.lambao.base.data.local

import com.lambao.base.data.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

abstract class BaseLocalDataSource(
    private val dispatcher: CoroutineDispatcher
) {
    protected open fun getUnknownErrorMessage() = "Unknown error"

    protected open fun getPermissionDeniedMessage() = "Permission denied"

    protected open fun getStorageUnavailableMessage() = "Storage unavailable"

    protected open fun getQueryFailedMessage() = "Failed to query local data"

    protected open fun <T> safeCall(localCall: suspend () -> T): Flow<Resource<T>> = flow {
        emit(Resource.Loading())
        val result = localCall()
        emit(Resource.Success(data = result))
    }.flowOn(dispatcher)
        .catch { e -> emit(Resource.Error(throwable = mapToLocalException(e))) }

    protected open fun mapToLocalException(e: Throwable): LocalException {
        return when (e) {
            is SecurityException -> LocalException(
                type = LocalErrorType.PERMISSION_DENIED,
                message = getPermissionDeniedMessage()
            )

            is IllegalStateException -> LocalException(
                type = LocalErrorType.QUERY_FAILED,
                message = getQueryFailedMessage()
            )

            is IOException -> LocalException(
                type = LocalErrorType.STORAGE_UNAVAILABLE,
                message = getStorageUnavailableMessage()
            )

            else -> LocalException(
                type = LocalErrorType.UNKNOWN,
                message = e.message ?: getUnknownErrorMessage()
            )
        }
    }
}