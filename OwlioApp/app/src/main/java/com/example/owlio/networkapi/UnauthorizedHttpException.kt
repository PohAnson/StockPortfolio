package com.example.owlio.networkapi

import retrofit2.HttpException

fun Result<ApiResult>.catchUnauthorizedHttpException(): Result<ApiResult> =
    recoverCatching { exception ->
        if (exception is HttpException && exception.code() == 401) {
            ApiResult.ApiError(401, "Incorrect Login Credentials")
        } else {
            throw exception
        }
    }
