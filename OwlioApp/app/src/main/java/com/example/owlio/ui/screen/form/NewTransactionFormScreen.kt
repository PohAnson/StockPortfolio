package com.example.owlio.ui.screen.form

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.owlio.ui.SnackbarDelegate
import com.example.owlio.ui.theme.OwlioAppTheme


@Composable
fun NewTransactionFormScreen(
    modifier: Modifier = Modifier,
    snackbarDelegate: SnackbarDelegate,
    navigateBack: () -> Unit,

    ) {
    val vm: TransactionFormViewModel = hiltViewModel()
    val uiState = vm.uiState.collectAsState().value
    val stockList = vm.getAllStockInfo().collectAsState(initial = listOf()).value


    TransactionForm(
        isEdit = false,
        uiState = uiState,
        stockList = stockList,
        vm = vm,
        modifier = modifier,
        snackbarDelegate = snackbarDelegate,
        navigateBack = navigateBack
    )
}


@Preview(showBackground = true)
@Composable
fun TransactionFormScreenPreview() {
    OwlioAppTheme {
        NewTransactionFormScreen(
            snackbarDelegate = SnackbarDelegate(SnackbarHostState()),
            navigateBack = {})
    }
}