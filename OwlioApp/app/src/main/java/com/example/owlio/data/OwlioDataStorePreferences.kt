package com.example.owlio.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.IOException
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class OwlioDataStorePreferences @Inject constructor(private val dataStorePreferences: DataStore<Preferences>) {


    val sessionId: String?
        get() = getPreferenceFor(SESSIONID_PREFKEY)
    val lastSyncedDateTime: ZonedDateTime
        get() = getPreferenceFor(LAST_SYNCED_DATETIME_PREFKEY)?.let {
            ZonedDateTime.parse(it)
        } ?: Instant.EPOCH.atZone(ZoneId.of("UTC"))

    private fun <T> getPreferenceFor(pref: Preferences.Key<T>): T? {
        return runBlocking(Dispatchers.IO) {
            dataStorePreferences.data.catch {
                handleIoException(it)
                emit(emptyPreferences())
            }.map { preferences ->
                preferences[pref]
            }.firstOrNull()
        }
    }

    suspend fun saveSessionId(sessionId: String) {
        dataStorePreferences.edit { preferences ->
            preferences[SESSIONID_PREFKEY] = sessionId
        }
    }

    suspend fun clearUserSessionId() {
        dataStorePreferences.edit { it.remove(SESSIONID_PREFKEY) }
    }

    suspend fun updateLastSyncedToNow() {
        dataStorePreferences.edit { preferences ->
            preferences[LAST_SYNCED_DATETIME_PREFKEY] =
                ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
        }
    }


    private fun handleIoException(throwable: Throwable) {
        if (throwable is IOException) {
            Timber.e(throwable)
        } else {
            throw throwable
        }
    }

    companion object {
        val SESSIONID_PREFKEY = stringPreferencesKey("session_id")
        val LAST_SYNCED_DATETIME_PREFKEY = stringPreferencesKey("last_synced_datetime")
    }
}
