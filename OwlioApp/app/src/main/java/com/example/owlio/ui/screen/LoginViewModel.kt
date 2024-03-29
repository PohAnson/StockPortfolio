package com.example.owlio.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owlio.data.UserCredentialRepo
import com.example.owlio.networkapi.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

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


    fun isCredentialPresent(): Boolean {
        return !userCredentialRepo.getCurrentSessionId().isNullOrEmpty()
    }

    fun authLogin(username: String, password: String): Job {
        authStatusUiState = AuthStatusUiState.Loading
        return viewModelScope.launch {
            val loginResult = userCredentialRepo.authLogin(username, password)
            authStatusUiState = when (loginResult) {
                is ApiResult.ApiError -> AuthStatusUiState.Error(
                    loginResult.message ?: "Login Error"
                )

                is ApiResult.ApiSuccess<*> -> AuthStatusUiState.Success(loginResult.data as String)
            }
            Timber.d(authStatusUiState.toString())
        }
    }

    fun login(sessionId: String) {
        userCredentialRepo.login(sessionId)
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
            Timber.d(authStatusUiState.toString())
        }
    }

    fun logout() {
        userCredentialRepo.logout()
    }
}
