package com.example.owlio.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_info")
data class StockInfo(
    @ColumnInfo(name = "trading_name") val tradingName: String,
    @PrimaryKey @ColumnInfo(name = "trading_code") val tradingCode: String,
    val ric: String?,
    val mkt_cap: Double?,
    val revenue: Double?,
    @ColumnInfo(name = "pe_ratio") val peRatio: Double?,
    @ColumnInfo(name = "yield_percent") val yieldPercent: Double?,
    val sector: String?,
    @ColumnInfo(name = "gti_score") val gtiScore: Double?,

    )