package com.lambao.base.presentation.handler.network_error

import com.lambao.base.data.remote.NetworkException

interface NetworkErrorHandler {
    fun handleError(networkException: NetworkException)
}