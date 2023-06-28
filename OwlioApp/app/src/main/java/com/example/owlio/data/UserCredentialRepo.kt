package com.example.owlio.data

import android.util.Log
import com.example.owlio.networkapi.ApiResult
import com.example.owlio.networkapi.ServerErrorResult
import com.example.owlio.networkapi.UserApiService
import com.example.owlio.networkapi.UserSessionIdResult
import com.example.owlio.networkapi.catchNoNetworkException
import com.example.owlio.networkapi.catchUnauthorizedHttpException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

class UserCredentialRepo @Inject constructor(
    private val owlioDataStorePreferences: OwlioDataStorePreferences,
    private val userApiService: UserApiService
) {
    fun getCurrentSessionId(): String? {
        return owlioDataStorePreferences.sessionId
    }

    suspend fun saveSessionId(sessionId: String) {
        owlioDataStorePreferences.saveSessionId(sessionId)
    }

    suspend fun clearUserSessionId() {
        owlioDataStorePreferences.clearUserSessionId()
    }


    private val json = Json { ignoreUnknownKeys = true }

    suspend fun checkUserSession(): ApiResult {
        return runCatching {
            ApiResult.ApiSuccess(
                userApiService.userSessionCheck().isLogin
            )
        }.catchNoNetworkException().catchUnauthorizedHttpException().getOrThrow()
    }

    suspend fun login(username: String, password: String): ApiResult {
        val jsonObject = buildJsonObject {
            put("username", username)
            put("password", password)
        }
        val requestBody =
            RequestBody.create(MediaType.get("application/json"), jsonObject.toString())

        return kotlin.runCatching {
            ApiResult.ApiSuccess(
                json.decodeFromString(
                    UserSessionIdResult.serializer(), userApiService.userLogin(requestBody).string()
                ).sessionid
            )
        }.catchNoNetworkException(
        ).catchUnauthorizedHttpException().getOrThrow()
    }

    suspend fun signup(username: String, password: String): ApiResult {
        val jsonObject = buildJsonObject {
            put("username", username)
            put("password", password)
            put("name", username) // TODO: add form field for name
        }
        val requestBody =
            RequestBody.create(MediaType.get("application/json"), jsonObject.toString())

        return kotlin.runCatching {
            ApiResult.ApiSuccess(
                json.decodeFromString(
                    UserSessionIdResult.serializer(),
                    userApiService.userSignup(requestBody).string()
                ).sessionid
            )
        }.catchNoNetworkException(
        ).catchUnauthorizedHttpException().recoverCatching { exception ->
            // catch Invalid credentials
            if (exception is HttpException && exception.code() == 406) {
                ApiResult.ApiError(
                    406, json.decodeFromString(
                        ServerErrorResult.serializer(),
                        exception.response()?.errorBody()?.string()
                            ?: """{"error": "Signup Error"}"""
                    ).error
                )
            } else {
                throw exception
            }
        }.getOrThrow()
    }


    suspend fun logout(): ApiResult {
        return try {
            userApiService.userLogout().let { ApiResult.ApiSuccess(it) }
        } catch (e: ConnectException) {
            Log.e(TAG, "CONNECT EXCEPTION " + e.toString())
            ApiResult.ApiError(503, "Server Unavailable")
        } catch (e: Throwable) {
            Log.e(TAG, "EXCEPTION " + e.toString())
            ApiResult.ApiError(500, e.toString())
        }
    }
}