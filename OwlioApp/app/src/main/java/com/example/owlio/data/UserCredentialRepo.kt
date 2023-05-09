package com.example.owlio.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class UserCredentialRepo @Inject constructor(private val dataStorePreferences: DataStore<Preferences>) {
    suspend fun saveUserCredentials(username: String, password: String) {
        dataStorePreferences.edit { preferences ->
            preferences[USERNAME] = username
            preferences[PASSWORD] = password
        }
    }

    val username = dataStorePreferences.data.catch {
        handleIoException(it)
        emit(emptyPreferences())
    }.map { preferences ->
        preferences[USERNAME]
    }
    val password = dataStorePreferences.data.catch {
        handleIoException(it)
        emit(emptyPreferences())
    }.map { preferences ->
        preferences[PASSWORD]
    }


    private companion object {

        fun handleIoException(throwable: Throwable) {
            if (throwable is IOException) {
                Log.e(TAG, "Error reading preferences.", throwable)
            } else {
                throw throwable
            }
        }

        val TAG = "USER SHARED PREF"
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
    }

}