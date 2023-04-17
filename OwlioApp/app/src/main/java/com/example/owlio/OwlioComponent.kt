package com.example.owlio

import android.content.Context
import com.example.owlio.data.StockInfoDao
import com.example.owlio.data.StockInfoDatabase
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Component(modules = [OwlioActivityModule::class])
interface OwlioComponent {
//    @Provides
//    @Singleton
//    fun getStockInfoRepo(): StockInfoRepo

//    fun inject(activity: MainActivity)

}


@Module
@InstallIn(SingletonComponent::class)
class OwlioActivityModule {
    @Provides
    @Singleton
    fun stockInfoDatabase(@ApplicationContext context: Context): StockInfoDatabase {
        return StockInfoDatabase.getDatabase(context)
    }

    @Provides
    fun stockInfoDao(@ApplicationContext context: Context): StockInfoDao {
        return stockInfoDatabase(context).stockInfoDao()
    }


}