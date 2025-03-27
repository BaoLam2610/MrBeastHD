package com.lambao.base.data

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val throwable: Throwable? = null
) {
    class Init<T> : Resource<T>()
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

inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Init -> Resource.Init()
        is Resource.Loading -> Resource.Loading(data?.let(transform))
        is Resource.Success -> Resource.Success(data?.let(transform), message)
        is Resource.Error -> Resource.Error(
            data?.let(transform),
            message,
            throwable ?: Throwable("Unknown error")
        )
    }
}

inline fun <T, R> Resource<List<T>>.mapList(transform: (T) -> R): Resource<List<R>> {
    return when (this) {
        is Resource.Init -> Resource.Init()
        is Resource.Loading -> Resource.Loading(data?.map(transform))
        is Resource.Success -> Resource.Success(data?.map(transform), message)
        is Resource.Error -> Resource.Error(
            data?.map(transform),
            message,
            throwable ?: Throwable("Unknown error")
        )
    }
}