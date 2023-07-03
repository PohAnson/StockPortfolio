package com.example.owlio.data

import com.example.owlio.networkapi.ApiResult
import com.example.owlio.networkapi.ServerErrorResult
import com.example.owlio.networkapi.UserApiService
import com.example.owlio.networkapi.UserSessionIdResult
import com.example.owlio.networkapi.catchNoNetworkException
import com.example.owlio.networkapi.catchUnauthorizedHttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import javax.inject.Inject

class UserCredentialRepo @Inject constructor(
    private val owlioDataStorePreferences: OwlioDataStorePreferences,
    private val userApiService: UserApiService
) {
    fun getCurrentSessionId(): String? {
        return owlioDataStorePreferences.sessionId
    }

    fun login(sessionId: String) {
        runBlocking(Dispatchers.IO) {
            owlioDataStorePreferences.saveSessionId(sessionId)
            owlioDataStorePreferences.resetLastSyncedToEpoch()  // to pull all data
        }
    }


    private val json = Json { ignoreUnknownKeys = true }

    suspend fun checkUserSession(): ApiResult {
        return runCatching {
            ApiResult.ApiSuccess(
                userApiService.userSessionCheck().isLogin
            )
        }.catchNoNetworkException().catchUnauthorizedHttpException().getOrThrow()
    }

    suspend fun authLogin(username: String, password: String): ApiResult {
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


    fun logout(): ApiResult {
        return runBlocking(Dispatchers.IO) {
            try {
                userApiService.userLogout().let { ApiResult.ApiSuccess(it) }
            } catch (e: ConnectException) {
                Timber.e(e)
                ApiResult.ApiError(503, "Server Unavailable")
            } catch (e: Throwable) {
                Timber.e(e)
                ApiResult.ApiError(500, e.toString())
            } finally {
                owlioDataStorePreferences.onUserLogout()
            }
        }
    }
}
