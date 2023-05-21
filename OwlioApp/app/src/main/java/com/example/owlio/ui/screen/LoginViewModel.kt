package com.example.owlio.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owlio.data.UserCredentialRepo
import com.example.owlio.networkapi.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private val TAG = "LOGIN VIEW MODEL"

sealed interface AuthStatusUiState {
    data class Success(val sessionId: String) : AuthStatusUiState
    object Loading : AuthStatusUiState
    data class Error(val errorMessage: String) : AuthStatusUiState
    object Default : AuthStatusUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(private val userCredentialRepo: UserCredentialRepo) :
    ViewModel() {
    var authStatusUiState: AuthStatusUiState by mutableStateOf(AuthStatusUiState.Default)

    fun saveSessionId(sessionId: String): Job {
        return viewModelScope.launch { userCredentialRepo.saveSessionId(sessionId) }
    }

    fun isCredentialPresent(): Boolean {
        return runBlocking(Dispatchers.Default) {
            !(userCredentialRepo.sessionId.firstOrNull()
                .isNullOrEmpty())
        }
    }

    fun login(username: String, password: String): Job {
        authStatusUiState = AuthStatusUiState.Loading
        return viewModelScope.launch {
            val loginResult = userCredentialRepo.login(username, password)
            authStatusUiState = when (loginResult) {
                is ApiResult.ApiError -> AuthStatusUiState.Error(
                    loginResult.message ?: "Login Error"
                )

                is ApiResult.ApiSuccess<*> -> AuthStatusUiState.Success(loginResult.data as String)
            }
            Log.d(TAG, authStatusUiState.toString())
        }
    }

    fun signup(username: String, password: String): Job {
        authStatusUiState = AuthStatusUiState.Loading
        return viewModelScope.launch {
            val signupResult = userCredentialRepo.signup(username, password)
            authStatusUiState = when (signupResult) {
                is ApiResult.ApiError -> AuthStatusUiState.Error(
                    signupResult.message ?: "Signup Error"
                )

                is ApiResult.ApiSuccess<*> -> AuthStatusUiState.Success(signupResult.data as String)
            }
            Log.d(TAG, authStatusUiState.toString())
        }
    }

    fun logout() {
        viewModelScope.launch {
            clearCredential()
            userCredentialRepo.logout()
        }
    }

    fun clearCredential() {
        runBlocking { userCredentialRepo.clearUserCredentials() }
    }

}