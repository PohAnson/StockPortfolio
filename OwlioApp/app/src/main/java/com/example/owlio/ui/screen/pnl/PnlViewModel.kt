package com.example.owlio.ui.screen.pnl

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owlio.data.DividendInfoRepo
import com.example.owlio.data.StockInfoRepo
import com.example.owlio.data.TransactionRepo
import com.example.owlio.model.PnlRowData
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
class PnlViewModel @Inject constructor(
    private val dividendInfoRepo: DividendInfoRepo,
    private val transactionRepo: TransactionRepo,
    stockInfoRepo: StockInfoRepo
) : ViewModel() {
    var isLoading by mutableStateOf(true)
    val stockInfoMapping: Flow<Map<String, StockInfo>> = stockInfoRepo.getAllStock()
        .mapLatest { stockInfo -> stockInfo.associateBy { it.tradingCode } }
    val pnlRow = getAllPnlRow()


    private fun getAllPnlRow(): StateFlow<List<PnlRowData>> {
        return transactionRepo.getAllTransaction().mapLatest { allTransaction ->
            isLoading = true
            val ledger = StockLedger()
            ledger.addTransactions(allTransaction)
            val tabulatedTransaction = ledger.tabulateAllTransactionForPnl(dividendInfoRepo)
            tabulatedTransaction.map {
                PnlRowData(
                    stockInfoMapping.first().let { stockInfoMap: Map<String, StockInfo> ->
                        stockInfoMap[it.key]?.tradingName ?: ""
                    },
                    it.key,
                    it.value["pnl"] as Float,
                    it.value["dividend"] as Float,
                )
            }.also {
                isLoading = false
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())
    }
}
