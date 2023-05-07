package com.example.owlio.ui.screen.form.transactionFormField

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.owlio.model.TradeType


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