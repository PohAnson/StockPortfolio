package com.example.owlio.ui.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owlio.data.StockInfoRepo
import com.example.owlio.data.TransactionRepo
import com.example.owlio.model.StockInfo
import com.example.owlio.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepo: TransactionRepo, stockInfoRepo: StockInfoRepo
) : ViewModel() {
    val transactionListState: StateFlow<List<Transaction>> =
        transactionRepo.getAllTransaction().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = listOf()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val stockInfoMapping: Flow<Map<String, StockInfo>> = stockInfoRepo.getAllStock()
        .mapLatest { stockInfo -> stockInfo.associateBy { it.tradingCode } }

    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            transactionRepo.deleteTransaction(transactionId)
        }
    }
}

