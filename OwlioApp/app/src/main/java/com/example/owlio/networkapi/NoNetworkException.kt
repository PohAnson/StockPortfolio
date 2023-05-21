package com.example.owlio.networkapi

import java.net.ConnectException
import java.net.SocketTimeoutException

fun Result<ApiResult>.catchNoNetworkException(): Result<ApiResult> = recoverCatching { exception ->
    if (exception is SocketTimeoutException || exception is ConnectException) {
        ApiResult.ApiError(503, "Server Unavailable")
    } else {
        throw exception
    }
}

