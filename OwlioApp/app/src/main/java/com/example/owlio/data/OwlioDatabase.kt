package com.example.owlio.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.owlio.model.StockInfo
import com.example.owlio.model.Transaction

@Database(entities = [StockInfo::class, Transaction::class], version = 1, exportSchema = false)
abstract class OwlioDatabase : RoomDatabase() {
    abstract fun stockInfoDao(): StockInfoDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: OwlioDatabase? = null

        fun getDatabase(context: Context): OwlioDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext, OwlioDatabase::class.java, "stock_info"
                ).fallbackToDestructiveMigration().createFromAsset("databases/stock_info.db")
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }

}