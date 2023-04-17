package com.example.owlio.data

import androidx.room.Dao
import androidx.room.Query
import com.example.owlio.model.StockInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface StockInfoDao {
    @Query("SELECT * FROM stock_info")
    fun getAllStockInfo(): Flow<List<StockInfo>>

}