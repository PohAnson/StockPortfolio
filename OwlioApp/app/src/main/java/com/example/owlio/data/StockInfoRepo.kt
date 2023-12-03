package com.example.owlio.data

import com.example.owlio.networkapi.StockPriceApiService
import timber.log.Timber
import javax.inject.Inject

class StockInfoRepo @Inject constructor(
    private val stockInfoDao: StockInfoDao,
    private val stockPriceApiService: StockPriceApiService
) {
    fun getAllStock() = stockInfoDao.getAllStockInfo()
    suspend fun getStockInfoByCode(tradingCode: String) =
        stockInfoDao.getStockInfoByCode(tradingCode = tradingCode)

    suspend fun getCurrentPrice(stockCode: String): Float {
        return try {
            val response = stockPriceApiService.getCurrentPrice("$stockCode.SI")
            response.body()?.chart?.result?.firstOrNull()?.meta?.regularMarketPrice
                ?: -1f
        } catch (e: Exception) {
            Timber.e(e, "getCurrentPrice $stockCode")
            -1f
        }

    }
}