package com.example.owlio

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.owlio.data.DeletedTransactionDao
import com.example.owlio.data.OwlioDataStorePreferences
import com.example.owlio.data.OwlioDatabase
import com.example.owlio.data.StockInfoDao
import com.example.owlio.data.TransactionDao
import com.example.owlio.networkapi.NetworkApiSessionCookiesInterceptor
import com.example.owlio.networkapi.TransactionSyncApiService
import com.example.owlio.networkapi.UserApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Component(modules = [OwlioActivityModule::class])
interface OwlioComponent


@Module
@InstallIn(SingletonComponent::class)
class OwlioActivityModule {
    @Provides
    @Singleton
    fun owlioDatabase(@ApplicationContext context: Context): OwlioDatabase {
        return OwlioDatabase.getDatabase(context)
    }

    @Provides
    fun stockInfoDao(@ApplicationContext context: Context): StockInfoDao {
        return owlioDatabase(context).stockInfoDao()
    }

    @Provides
    fun transactionDao(@ApplicationContext context: Context): TransactionDao {
        return owlioDatabase(context).transactionDao()
    }

    @Provides
    fun deletedTransactionDao(@ApplicationContext context: Context): DeletedTransactionDao {
        return owlioDatabase(context).deletedTransactionDao()
    }

}

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {
    private val BASE_URL = "http://192.168.1.200:5000/api/v0/"

    private val jsonConverterFactory = Json { ignoreUnknownKeys = true }


    @Provides
    @Singleton
    fun getInterceptor(owlioDataStorePreferences: OwlioDataStorePreferences): NetworkApiSessionCookiesInterceptor {
        return NetworkApiSessionCookiesInterceptor(owlioDataStorePreferences)
    }

    @Provides
    @Singleton
    fun getOkHttpClient(interceptor: NetworkApiSessionCookiesInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun retrofitService(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            jsonConverterFactory.asConverterFactory(MediaType.get("application/json"))
        )
        .client(httpClient)
        .build()


    @Provides
    @Singleton
    fun userRetrofitService(retrofitService: Retrofit): UserApiService {
        return retrofitService.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun transactionSyncApiRetrofitService(retrofitService: Retrofit): TransactionSyncApiService {
        return retrofitService.create(TransactionSyncApiService::class.java)
    }
}

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
