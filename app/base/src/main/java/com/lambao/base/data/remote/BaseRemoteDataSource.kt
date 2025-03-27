package com.lambao.base.data.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lambao.base.data.Resource
import com.lambao.base.utils.log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRemoteDataSource(
    private val jsonParser: Gson = Gson(),
    private val dispatcher: CoroutineDispatcher
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        log(throwable.message ?: "")
    }

    suspend fun <T> safeCall(apiCall: suspend () -> Response<T>): Flow<Resource<T>> = flow {
        emit(Resource.Loading())
        val response = apiCall()
        emit(
            if (response.isSuccessful) {
                Resource.Success(data = response.body())
            } else {
                Resource.Error(throwable = parseErrorResponse(response))
            }
        )
    }.flowOn(dispatcher + exceptionHandler)
        .catch { e -> emit(Resource.Error(throwable = mapExceptionToNetworkError(e))) }

    suspend fun <T> safeApiCall(apiCall: suspend () -> ApiResponse<T>): Flow<Resource<T>> = flow {
        emit(Resource.Loading())
        val response = apiCall()
        emit(
            if (response.status == true) {
                Resource.Success(data = response.data)
            } else {
                Resource.Error(throwable = parseErrorResponse(response))
            }
        )
    }.flowOn(dispatcher + exceptionHandler)
        .catch { e -> emit(Resource.Error(throwable = mapExceptionToNetworkError(e))) }

    private fun <T> parseErrorResponse(response: Response<T>): NetworkException {
        return try {
            val type = object : TypeToken<ApiResponse<T>>() {}.type
            val errorResponse: ApiResponse<T>? =
                jsonParser.fromJson(response.errorBody()?.charStream(), type)
            val code = errorResponse?.code ?: response.code()
            val message = errorResponse?.message ?: response.message()
            mapToNetworkException(code, message)
        } catch (e: Exception) {
            NetworkException(
                type = NetworkErrorType.UNKNOWN,
                code = response.code(),
                message = e.message ?: response.message()
            )
        }
    }

    private fun <T> parseErrorResponse(response: ApiResponse<T>): NetworkException {
        val code = response.code ?: 0
        val message = response.message ?: "Unknown error"
        return mapToNetworkException(code, message)
    }

    private fun mapToNetworkException(code: Int, message: String): NetworkException {
        return when (code) {
            400 -> NetworkException(NetworkErrorType.BAD_REQUEST, code, message)
            401 -> NetworkException(NetworkErrorType.UNAUTHORIZED, code, message)
            403 -> NetworkException(NetworkErrorType.FORBIDDEN, code, message)
            404 -> NetworkException(NetworkErrorType.NOT_FOUND, code, message)
            429 -> NetworkException(NetworkErrorType.TOO_MANY_REQUESTS, code, message)
            500 -> NetworkException(NetworkErrorType.SERVER_ERROR, code, message)
            502 -> NetworkException(NetworkErrorType.BAD_GATEWAY, code, message)
            503 -> NetworkException(NetworkErrorType.SERVICE_UNAVAILABLE, code, message)
            else -> NetworkException(NetworkErrorType.UNKNOWN, code, message)
        }
    }

    private fun mapExceptionToNetworkError(e: Throwable): NetworkException {
        return when (e) {
            is HttpException -> NetworkException(
                type = when (e.code()) {
                    400 -> NetworkErrorType.BAD_REQUEST
                    401 -> NetworkErrorType.UNAUTHORIZED
                    403 -> NetworkErrorType.FORBIDDEN
                    404 -> NetworkErrorType.NOT_FOUND
                    429 -> NetworkErrorType.TOO_MANY_REQUESTS
                    500 -> NetworkErrorType.SERVER_ERROR
                    502 -> NetworkErrorType.BAD_GATEWAY
                    503 -> NetworkErrorType.SERVICE_UNAVAILABLE
                    else -> NetworkErrorType.UNKNOWN
                },
                code = e.code(),
                message = e.message()
            )

            is IOException -> NetworkException(
                type = NetworkErrorType.NO_NETWORK,
                message = e.message ?: "No network connection"
            )

            else -> NetworkException(
                type = NetworkErrorType.UNKNOWN,
                message = e.message ?: "Unknown error"
            )
        }
    }
}