package com.lambao.base.data.remote

data class NetworkException(
    val type: NetworkErrorType,
    val code: Int? = null,
    override val message: String? = null
) : Throwable(message)