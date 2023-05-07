package com.example.owlio.model

data class PnlRowData(
    val stockName: String, val stockCode: String, val transaction: Float, val dividend: Float
) {
    val totalEarnings
        get() = transaction + dividend
}
