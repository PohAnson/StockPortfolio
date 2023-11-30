package com.example.owlio.model

data class PortfolioRowData(
    val stockName: String,
    val stockCode: String,
    val volume: Int,
    val cost: Float,
    val lastPrice: Float,
) {
    val marketValue: Float
        get() = volume * lastPrice
    val unrealisedPnl: Float
        get() = marketValue - cost
    val avgPrice: Float
        get() = if (volume == 0) 0f else cost / volume
}
