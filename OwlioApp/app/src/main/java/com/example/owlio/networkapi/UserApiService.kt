package com.example.owlio.networkapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

// Authenticate to the server

interface UserApiService {
    @GET("user")
    suspend fun userSessionCheck(): ResponseBody

    @POST("user")
    suspend fun userLogin(@Body requestBody: RequestBody): ResponseBody

    @PUT("user")
    suspend fun userSignup(@Body requestBody: RequestBody): ResponseBody

    @GET("user/logout")
    suspend fun userLogout(): ResponseBody
}

sealed interface ApiResult {
    data class ApiSuccess<T>(val data: T) : ApiResult
    data class ApiError(val code: Int, val message: String?) : ApiResult
}


@Serializable
data class UserSessionIdResult(@SerialName("sassyid") val sessionid: String)

@Serializable
data class ServerErrorResult(val error: String)