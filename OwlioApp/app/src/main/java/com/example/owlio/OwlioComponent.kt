package com.example.owlio

import android.content.Context
import com.example.owlio.data.OwlioDatabase
import com.example.owlio.data.StockInfoDao
import com.example.owlio.data.TransactionDao
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
}

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {
    private val BASE_URL = "http://192.168.1.200:5000/api/v0/"

    private val jsonCoverterFactory = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    @get:Provides
    val retrofitService: Retrofit = Retrofit.Builder().addConverterFactory(
        jsonCoverterFactory.asConverterFactory(
            MediaType.get("application/json")
        )
    ).baseUrl(BASE_URL).build()


    @get:Provides
    val userRetrofitService: UserApiService by lazy {
        retrofitService.create(UserApiService::class.java)
    }


}
