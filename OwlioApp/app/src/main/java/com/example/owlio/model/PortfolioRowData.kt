package com.example.owlio.model

data class PortfolioRowData(
    val stockName: String, val stockCode: String, val volume: Int, val cost: Float
) {
    val avgPrice: Float
        get() = if (volume == 0) 0f else cost / volume
}
