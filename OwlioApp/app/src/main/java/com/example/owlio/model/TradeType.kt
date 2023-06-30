package com.example.owlio.model

enum class TradeType {
    Sell, Buy, INVALID
}

fun tradeTypeFromString(tradeTypeString: String): TradeType {
    return when (tradeTypeString.lowercase()) {
        "sell" -> TradeType.Sell
        "buy" -> TradeType.Buy
        else -> throw Exception("Invalid tradeTypeString '$tradeTypeString' given")
    }

}