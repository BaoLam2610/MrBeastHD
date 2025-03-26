package com.lambao.base.ui.error_handler

import com.lambao.base.data.remote.NetworkException

interface NetworkErrorHandler {
    fun handleError(networkException: NetworkException)
}