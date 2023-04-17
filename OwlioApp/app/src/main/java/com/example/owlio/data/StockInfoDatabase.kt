package com.example.owlio.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.owlio.model.StockInfo

@Database(entities = [StockInfo::class], version = 1, exportSchema = false)
abstract class StockInfoDatabase : RoomDatabase() {
    abstract fun stockInfoDao(): StockInfoDao

    companion object {
        @Volatile
        private var INSTANCE: StockInfoDatabase? = null

        fun getDatabase(context: Context): StockInfoDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext, StockInfoDatabase::class.java, "stock_info"
                ).fallbackToDestructiveMigration().createFromAsset("databases/stock_info.db")
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }

}