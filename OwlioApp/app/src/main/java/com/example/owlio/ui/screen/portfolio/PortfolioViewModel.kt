package com.example.owlio.ui.screen.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owlio.data.StockInfoRepo
import com.example.owlio.data.TransactionRepo
import com.example.owlio.model.PortfolioRowData
import com.example.owlio.model.StockInfo
import com.example.owlio.model.StockLedger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PortfolioViewModel @Inject constructor(
    stockInfoRepo: StockInfoRepo, val transactionRepo: TransactionRepo
) : ViewModel() {
    val stockInfoMapping: Flow<Map<String, StockInfo>> = stockInfoRepo.getAllStock()
        .mapLatest { stockInfo -> stockInfo.associateBy { it.tradingCode } }
    val portfolioRowDataState = getPortfolioRows(tabulateTransactions(), stockInfoMapping)

    fun tabulateTransactions(): Flow<Map<String, Map<String, Any>>> {
        val ledger = StockLedger()
        return transactionRepo.getAllTransaction().mapLatest {
            ledger.addTransactions(it)
            ledger.tabulateAllTransactions()
        }
    }


    fun getPortfolioRows(
        tabulatedTransactions: Flow<Map<String, Map<String, Any>>>,
        stockInfoMapping: Flow<Map<String, StockInfo>>
    ): StateFlow<List<PortfolioRowData>> {
        return tabulatedTransactions.mapLatest { singleTabulatedTransaction ->
            singleTabulatedTransaction.map {
                PortfolioRowData(
                    stockInfoMapping.first().let { stockInfoMap: Map<String, StockInfo> ->
                        stockInfoMap[it.key]?.tradingName ?: ""
                    },
                    it.key,
                    it.value["volume"] as Int,
                    it.value["cost"] as Float,
                )
            }
        }.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), listOf()
        )
    }

}