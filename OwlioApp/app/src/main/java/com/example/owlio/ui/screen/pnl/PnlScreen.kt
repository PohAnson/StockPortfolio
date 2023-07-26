package com.example.owlio.ui.screen.pnl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.owlio.ui.LoadingAnimation

@Composable
fun PnlScreen(modifier: Modifier = Modifier) {
    val vm: PnlViewModel = hiltViewModel()
    val pnlRowDataList by vm.pnlRow.collectAsState()

    // calculate the total earnings
    val dividendEarnings = pnlRowDataList.fold(0f) { acc, pnlRowData -> acc + pnlRowData.dividend }
    val transactionEarnings =
        pnlRowDataList.fold(0f) { acc, pnlRowData -> acc + pnlRowData.transaction }

    if (vm.isLoading) {
        Box(Modifier.fillMaxSize()) {
            LoadingAnimation(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PnlCard(dividendEarnings = dividendEarnings, transactionEarnings = transactionEarnings)
            PnlTable(pnlRowDataList)
        }
    }
}