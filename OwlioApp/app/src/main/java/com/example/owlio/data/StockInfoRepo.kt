package com.example.owlio.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

class StockInfoRepo @Inject constructor(private val stockInfoDao: StockInfoDao) {
    fun getAllStock() = stockInfoDao.getAllStockInfo()
    suspend fun getStockInfoByCode(tradingCode: String) =
        stockInfoDao.getStockInfoByCode(tradingCode = tradingCode)

    fun getCurrentPrice(stockCode: String): Float {
        // assumed that negative current price means that it cannot be found
        val symbol = "$stockCode.SI"
        val doc = runBlocking {
            withContext(Dispatchers.Default) {
                Jsoup.connect("https://finance.yahoo.com/quote/${symbol}").get()
            }
        }
        return doc.selectXpath("//fin-streamer[@data-field='regularMarketPrice'][@data-symbol='${symbol}']")
            .text().ifEmpty { "-1" }.toFloat()
    }


}