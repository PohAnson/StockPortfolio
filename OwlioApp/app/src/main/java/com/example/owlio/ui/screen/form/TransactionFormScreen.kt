package com.example.owlio.ui.screen.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.owlio.model.TradeType
import com.example.owlio.ui.screen.form.transactionFormField.BrokerField
import com.example.owlio.ui.screen.form.transactionFormField.StockSelectorField
import com.example.owlio.ui.screen.form.transactionFormField.TradeDateField
import com.example.owlio.ui.theme.OwlioAppTheme

@Composable
fun TransactionFormScreen(modifier: Modifier=Modifier) {
    val vm: TransactionFormViewModel = viewModel()
    val uiState = vm.uiState.collectAsState().value

    Column(modifier = modifier.padding(8.dp,0.dp)) {

        TradeDateField(uiState.tradeDate) { vm.updateTradeDate(it) }
        StockSelectorField(
            vm.getAllStockInfo(), uiState.selectedStock
        ) { vm.updateSelectedStock(it) }
        BrokerField(uiState.broker) { vm.updateBroker(it) }
        TradeTypeField(uiState.tradeType) { vm.updateTradeType(it) }
        PriceField(uiState.price) { vm.updatePrice(it) }
        VolumeField(uiState.volume) { vm.updateVolume(it) }
        if (!uiState.errorMessage.isNullOrBlank()) {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(8.dp)
            ) {
                Text(uiState.errorMessage, color = Color.Red)
            }
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth(1f)) {
            Button(onClick = { vm.validateAllFields() }) {
                Text("Submit")
            }

        }
    }
}

@Composable
fun GenericFieldRow(
    label: String, spaceBelow: Boolean = true, inputField: @Composable (() -> Unit)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label, modifier = Modifier
                .fillMaxWidth(0.3f)
                .padding(end = 4.dp)
        )
        inputField()
    }
    if (spaceBelow) Spacer(modifier = Modifier.height(8.dp))
}


@Composable
fun TradeTypeField(tradeType: TradeType?, updateTradeType: (TradeType) -> Unit) {
    GenericFieldRow(label = "Trade Type") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val selectedButtonColors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
//            tradeType == TradeType.Buy
            OutlinedButton(
                onClick = { updateTradeType(TradeType.Buy) },
                modifier = Modifier.padding(8.dp),
                colors = if (tradeType == TradeType.Buy) selectedButtonColors else ButtonDefaults.outlinedButtonColors(),
            ) {
                Text("Buy")
            }
            OutlinedButton(
                onClick = { updateTradeType(TradeType.Sell) },
                colors = if (tradeType == TradeType.Sell) selectedButtonColors else ButtonDefaults.outlinedButtonColors(),
            ) {
                Text("Sell")
            }
        }
    }
}

@Composable
fun PriceField(price: String, updatePrice: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    GenericFieldRow(label = "Price") {
        OutlinedTextField(value = price,
            onValueChange = { updatePrice(it) },
            modifier = Modifier.onFocusChanged { focusState ->
                // round to 3 d.p. when loses focus on the field
                if (!focusState.isFocused) {
                    price.toFloatOrNull()?.let { value ->
                        updatePrice("%.3f".format(value))
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Next) })
    }
}

@Composable
fun VolumeField(volume: String, updateVolume: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    GenericFieldRow(label = "Volume") {
        OutlinedTextField(value = volume,
            onValueChange = { updateVolume(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions { focusManager.clearFocus() })
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionFormScreenPreview() {
    OwlioAppTheme {
        TransactionFormScreen()
    }
}