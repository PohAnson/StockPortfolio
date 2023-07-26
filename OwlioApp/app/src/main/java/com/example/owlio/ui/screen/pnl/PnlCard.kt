package com.example.owlio.ui.screen.pnl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PnlCard(dividendEarnings: Float, transactionEarnings: Float) {
    val totalEarnings = dividendEarnings + transactionEarnings
    Card(
        modifier = Modifier
            .fillMaxWidth(0.98f)
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column {
                Text(text = "Total P/L:")
                Text(text = "$$totalEarnings")
            }
            Column {
                Text(text = "Dividends:")
                Text(text = "$$dividendEarnings")
            }
            Column {
                Text(text = "Transactions:")
                Text(text = "$$transactionEarnings")
            }
        }

    }
}