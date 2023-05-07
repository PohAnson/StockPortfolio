package com.example.owlio.ui.screen.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.owlio.ui.SnackbarDelegate
import com.example.owlio.ui.SnackbarState
import com.example.owlio.ui.screen.form.transactionFormField.BrokerField
import com.example.owlio.ui.screen.form.transactionFormField.PriceField
import com.example.owlio.ui.screen.form.transactionFormField.StockSelectorField
import com.example.owlio.ui.screen.form.transactionFormField.TradeDateField
import com.example.owlio.ui.screen.form.transactionFormField.TradeTypeField
import com.example.owlio.ui.screen.form.transactionFormField.VolumeField
import com.example.owlio.ui.theme.OwlioAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun TransactionFormScreen(
    modifier: Modifier = Modifier,
    snackbarDelegate: SnackbarDelegate,
    navigateBack: () -> Unit,

    ) {
    val vm: TransactionFormViewModel = hiltViewModel()
    val uiState = vm.uiState.collectAsState().value
    val stockList = vm.getAllStockInfo().collectAsState(initial = listOf()).value

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current


    Column(modifier = modifier.padding(8.dp, 0.dp)) {
        TradeDateField(uiState.tradeDate) { vm.updateTradeDate(it) }
        StockSelectorField(
            stockList, uiState.selectedStock
        ) { vm.updateSelectedStock(it) }
        BrokerField(uiState.broker) { vm.updateBroker(it) }
        TradeTypeField(uiState.tradeType) { vm.updateTradeType(it) }
        PriceField(uiState.price) { vm.updatePrice(it) }
        VolumeField(uiState.volume) { vm.updateVolume(it) }
        if (uiState.submissionStatus is SubmissionStatus.Error) {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(8.dp)
            ) {
                Text(uiState.submissionStatus.errorMessage, color = Color.Red)
            }
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth(1f)) {
            Button(onClick = {
                focusManager.clearFocus()

                coroutineScope.launch {
                    val submissionStatus = withContext(Dispatchers.Default) {
                        vm.submitForm()
                    }
                    when (submissionStatus) {
                        is SubmissionStatus.Success -> snackbarDelegate.showSnackbar(snackbarState = SnackbarState.SUCCESS,
                            message = "Transaction Added",
                            actionLabel = "View",
                            duration = SnackbarDuration.Short,
                            onAction = { navigateBack() })

                        is SubmissionStatus.Error -> {
                            snackbarDelegate.showSnackbar(
                                snackbarState = SnackbarState.ERROR,
                                message = "Error: ${submissionStatus.errorMessage}",
                                duration = SnackbarDuration.Short
                            )
                        }

                        else -> {}
                    }
                }

            }) {
                Text("Submit")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TransactionFormScreenPreview() {
    OwlioAppTheme {
        TransactionFormScreen(snackbarDelegate = SnackbarDelegate(), navigateBack = {})
    }
}