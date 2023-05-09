package com.example.owlio

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@HiltAndroidApp
class OwlioApplication : Application()

private val Context.dataStorePreferences: DataStore<Preferences> by preferencesDataStore(
    name = "UserCredentialDataStore"
)

@Module
@InstallIn(SingletonComponent::class)
class DataStorePreferencesModule {
    @Provides
    @Singleton
    fun provideDataStorePreferences(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return appContext.dataStorePreferences
    }

}