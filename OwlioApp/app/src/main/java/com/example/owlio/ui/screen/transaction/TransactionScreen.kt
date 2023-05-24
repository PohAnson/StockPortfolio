package com.example.owlio.ui.screen.transaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.owlio.model.Transaction

@Composable
fun TransactionScreen(
    goToNewTransactionForm: () -> Unit,
    goToEditTransactionForm: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: TransactionViewModel = hiltViewModel()
    val transactionList: List<Transaction> = vm.transactionListState.collectAsState(listOf()).value
    val stockInfoMapping = vm.stockInfoMapping.collectAsState(initial = mapOf()).value

    Box(
        modifier = modifier.fillMaxSize(1f)
    ) {

        TransactionTable(
            transactionList = transactionList,
            stockInfoMapping = stockInfoMapping,
            goToEditTransactionForm = goToEditTransactionForm
        )

        FloatingActionButton(
            onClick = { goToNewTransactionForm() }, modifier = Modifier
                .align(
                    Alignment.BottomEnd
                )
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
        }
    }
}