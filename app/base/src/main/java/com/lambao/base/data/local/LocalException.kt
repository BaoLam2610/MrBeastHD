package com.lambao.base.data.local

data class LocalException(
    val type: LocalErrorType,
    override val message: String? = null
) : Throwable(message)