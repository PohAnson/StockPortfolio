package com.example.owlio.ui.screen.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.owlio.ui.SnackbarDelegate

@Composable
fun EditTransactionFormScreen(
    transactionId: String,
    modifier: Modifier = Modifier,
    snackbarDelegate: SnackbarDelegate,
    navigateBack: () -> Unit,
) {
    val vm: TransactionFormViewModel = hiltViewModel()
    val uiState = vm.uiState.collectAsState().value
    val stockList = vm.getAllStockInfo().collectAsState(initial = listOf()).value


    LaunchedEffect(key1 = transactionId) {
        // update the view model with the data for the current transaction
        vm.fillFormFromId(transactionId.toInt())

    }

    TransactionForm(
        isEdit = true,
        uiState = uiState,
        stockList = stockList,
        vm = vm,
        modifier = modifier,
        snackbarDelegate = snackbarDelegate,
        navigateBack = navigateBack
    )
}