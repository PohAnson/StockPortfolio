package com.example.owlio.model


class _SingleStockRecord(val code: String) {

    // sort the set in ascending order
    val transactionSet =
        sortedSetOf<Transaction>(comparator = Comparator.comparing({ it.tradeDate }))

    fun addTransaction(transaction: Transaction) {
        transactionSet.add(transaction)
    }

    fun tabulateTransaction(): Map<String, Any> {
        // sum of all the transactions till sold fully.
        var curTotalCost = 0f
        var curTotalVolume = 0
        var pnl = 0f  // the net profit/loss when vol is zero, +ve is profit

        for (transaction in transactionSet) {
            val value = transaction.price * transaction.volume
            val fees = transaction.calculateFees()

            when (transaction.tradeType) {
                TradeType.Buy -> {
                    curTotalCost += value + fees
                    curTotalVolume += transaction.volume
                }

                TradeType.Sell -> {
                    // check if fully sold
                    if (curTotalVolume == transaction.volume) {
                        // when fully sold, save to pnl and reset the cost and vol
                        pnl += value - fees - curTotalCost
                        curTotalCost = 0f
                        curTotalVolume = 0
                    } else {
                        // percentage of cost based on volume for the transaction
                        val percentageCost = curTotalCost / curTotalVolume * transaction.volume
                        pnl += (value - percentageCost) - fees
                        curTotalCost -= percentageCost
                        curTotalVolume -= transaction.volume
                    }
                }

                else -> {}
            }
        }
        return mapOf("volume" to curTotalVolume, "cost" to curTotalCost, "pnl" to pnl)
    }

}


class StockLedger {
    val stockRecords: MutableMap<String, _SingleStockRecord> = mutableMapOf()  // Code: Recs

    fun addTransaction(transaction: Transaction) {
        if (!stockRecords.containsKey(transaction.stockCode)) {
            stockRecords[transaction.stockCode] = _SingleStockRecord(transaction.stockCode)
        }
        stockRecords[transaction.stockCode]?.addTransaction(transaction)
    }

    fun addTransactions(transactions: Iterable<Transaction>) {
        transactions.forEach { addTransaction(it) }
    }

    fun tabulateAllTransactions(): Map<String, Map<String, Any>> {
        // stockCode: {volume, cost, pnl}
        return stockRecords.mapValues { it.value.tabulateTransaction() }
    }
}

