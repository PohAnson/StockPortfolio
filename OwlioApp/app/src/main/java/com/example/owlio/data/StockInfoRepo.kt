package com.example.owlio.data

import javax.inject.Inject

class StockInfoRepo @Inject constructor(private val stockInfoDao: StockInfoDao) {
    fun getAllStock() = stockInfoDao.getAllStockInfo()
}