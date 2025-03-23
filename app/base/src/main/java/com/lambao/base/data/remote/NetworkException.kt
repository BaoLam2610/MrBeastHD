package com.lambao.base.data.remote

class NetworkException(
    val type: NetworkErrorType,
    val code: Int? = null,
    message: String? = null
) : Throwable(message) {
    override val message: String?
        get() = super.message ?: "Unknown network error"
}