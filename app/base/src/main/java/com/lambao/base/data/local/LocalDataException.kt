package com.lambao.base.data.local

data class LocalDataException(
    val type: LocalErrorType,
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception(message, cause)