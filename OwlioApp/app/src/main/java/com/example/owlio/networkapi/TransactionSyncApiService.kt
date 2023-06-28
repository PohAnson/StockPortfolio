package com.example.owlio.networkapi

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface TransactionSyncApiService {
    @POST("syncTransaction")
    suspend fun syncTransaction(@Body requestBody: RequestBody): ResponseBody
}