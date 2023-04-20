package com.example.owlio.ui.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owlio.data.TransactionRepo
import com.example.owlio.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(transactionRepo: TransactionRepo) : ViewModel() {
    val transactionListState: StateFlow<List<Transaction>> =
        transactionRepo.getAllTransaction().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = listOf()
        )
}

