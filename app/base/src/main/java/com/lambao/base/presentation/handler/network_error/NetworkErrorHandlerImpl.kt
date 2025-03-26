package com.lambao.base.presentation.handler.network_error

import android.content.Context
import com.lambao.base.data.remote.NetworkErrorType
import com.lambao.base.data.remote.NetworkException
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.utils.log

class NetworkErrorHandlerImpl(
    private val context: Context,
    private val dialogHandler: DialogHandler
) : NetworkErrorHandler {
    override fun handleError(networkException: NetworkException) {
        val message = when (networkException.type) {
            NetworkErrorType.UNAUTHORIZED -> "Error 401: Please login again"
            NetworkErrorType.NOT_FOUND -> "Error 404: Data not found"
            NetworkErrorType.SERVER_ERROR -> "Error 500: Server issue"
            NetworkErrorType.NO_NETWORK -> "No internet connection"
            NetworkErrorType.TOO_MANY_REQUESTS -> "Error 429: Too many requests"
            NetworkErrorType.BAD_REQUEST -> "Error 400: Invalid request"
            NetworkErrorType.FORBIDDEN -> "Error 403: Access denied"
            NetworkErrorType.BAD_GATEWAY -> "Error 502: Bad gateway"
            NetworkErrorType.SERVICE_UNAVAILABLE -> "Error 503: Service unavailable"
            NetworkErrorType.UNKNOWN -> "Unknown error: ${networkException.message}"
        }
        log(message)
        dialogHandler.showMessageDialog(message)
    }
}