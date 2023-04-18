package com.example.owlio

import android.content.Context
import com.example.owlio.data.OwlioDatabase
import com.example.owlio.data.StockInfoDao
import com.example.owlio.data.TransactionDao
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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