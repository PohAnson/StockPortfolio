package com.example.owlio.data

import com.example.owlio.model.DividendDateRow
import com.example.owlio.model.TradeType
import com.example.owlio.model.Transaction
import com.example.owlio.utils.toLocalDate
import java.time.LocalDate


object TransactionDividendDataSource {
    fun calculateTotalDividendEarnings(
        dividendRows: MutableList<DividendDateRow>, allTransaction: List<Transaction>
    ): Float {
        val breakdown = getDividendsEarningBreakdown(dividendRows, allTransaction)
        return breakdown.fold(0f) { acc, dividendEarningRowInfo -> acc + dividendEarningRowInfo.value }

    }

    private fun getDividendsEarningBreakdown(
        dividendRows: MutableList<DividendDateRow>, allTransaction: List<Transaction>
    ): MutableList<DividendEarningRowInfo> {
        if (dividendRows.isEmpty()) {
            return mutableListOf()
        }
        var curRow = dividendRows.removeFirst()
        val breakdown = mutableListOf<DividendEarningRowInfo>()
        for (transactionRange in generateRangeList(allTransaction)) {
            // consume the rows till it reaches the start range
            while (curRow.exdate < transactionRange.start && dividendRows.isNotEmpty()) {
                curRow = dividendRows.removeFirst()
            }

            while (curRow.exdate in transactionRange) {
                breakdown.add(
                    DividendEarningRowInfo(
                        curRow.exdate,
                        curRow.rate,
                        transactionRange.volume,
                        curRow.rate * transactionRange.volume
                    )
                )
                if (dividendRows.isEmpty()) {
                    break
                } else {
                    curRow = dividendRows.removeFirst()
                }
            }
        }
        return breakdown
    }

    private fun generateRangeList(allTransaction: List<Transaction>): List<TransactionVolumeRange> {
        val completeRangeList = mutableListOf<TransactionVolumeRange>()
        var incompleteRange: TransactionVolumeRange? = null

        for (transaction in allTransaction) {
            val localTradeDate = transaction.tradeDate.toLocalDate()
            when (transaction.tradeType) {
                TradeType.Buy -> {
                    if (incompleteRange == null) {
                        incompleteRange = TransactionVolumeRange(
                            localTradeDate, localTradeDate, transaction.volume
                        )
                    } else {
                        // 'finish' the range and open a new combined range
                        incompleteRange.end = localTradeDate
                        completeRangeList.add(incompleteRange)
                        incompleteRange = TransactionVolumeRange(
                            localTradeDate,
                            localTradeDate,
                            incompleteRange.volume + transaction.volume
                        )
                    }
                }

                TradeType.Sell -> {
                    if (incompleteRange != null) {

                        incompleteRange.end = localTradeDate
                        completeRangeList.add(incompleteRange)

                        // assign incomplete range based on whether it is fully sold or not.
                        incompleteRange = if (incompleteRange.volume == transaction.volume) {
                            null
                        } else {
                            TransactionVolumeRange(
                                localTradeDate,
                                localTradeDate,
                                incompleteRange.volume - transaction.volume
                            )
                        }
                    }
                }

                else -> {}
            }
        }
        if (incompleteRange != null) {
            incompleteRange.end = LocalDate.now()
            completeRangeList.add(incompleteRange)
        }
        return completeRangeList
    }

}

private data class TransactionVolumeRange(
    val start: LocalDate, var end: LocalDate, val volume: Int
) {
    operator fun contains(exdate: LocalDate): Boolean {
        return (start <= exdate && exdate < end)
    }
}

private data class DividendEarningRowInfo(
    val exdate: LocalDate, val rate: Float, val volume: Int, val value: Float
)