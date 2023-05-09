package com.example.owlio.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owlio.data.UserCredentialRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userCredentialRepo: UserCredentialRepo) :
    ViewModel() {
    fun saveCredential(username: String, password: String) {
        viewModelScope.launch { userCredentialRepo.saveUserCredentials(username, password) }
    }

    fun isCredentialPresent(): Boolean {
        return runBlocking(Dispatchers.Default) {
            !(userCredentialRepo.username.firstOrNull()
                .isNullOrEmpty() || userCredentialRepo.password.firstOrNull().isNullOrEmpty())
        }
    }

    fun checkCredential(username: String, password: String): Boolean {
        return runBlocking(Dispatchers.Default) {
            (userCredentialRepo.username.firstOrNull() == username) && (userCredentialRepo.password.firstOrNull() == password)
        }
    }

    fun clearCredential() {
        runBlocking { userCredentialRepo.clearUserCredentials() }
    }

}