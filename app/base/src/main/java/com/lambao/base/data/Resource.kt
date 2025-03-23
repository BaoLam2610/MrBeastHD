package com.lambao.base.data

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val throwable: Throwable? = null
) {
    class Loading<T>(data: T? = null) : Resource<T>(data = data)

    class Success<T>(data: T?, message: String? = null) :
        Resource<T>(
            data = data,
            message = message
        )

    class Error<T>(data: T? = null, message: String? = null, throwable: Throwable) :
        Resource<T>(
            data = data,
            message = message,
            throwable = throwable
        )
}